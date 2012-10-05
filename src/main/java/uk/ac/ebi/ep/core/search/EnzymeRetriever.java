package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.adapter.bioportal.BioportalAdapterException;
import uk.ac.ebi.ep.adapter.bioportal.BioportalWsAdapter;
import uk.ac.ebi.ep.adapter.bioportal.IBioportalAdapter;
import uk.ac.ebi.ep.adapter.chebi.ChebiAdapter;
import uk.ac.ebi.ep.adapter.chebi.ChebiFetchDataException;
import uk.ac.ebi.ep.adapter.chebi.IChebiAdapter;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;
import uk.ac.ebi.ep.adapter.das.SimpleDASFeaturesAdapter;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.Domains;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.FieldsOfPdbe;
import uk.ac.ebi.ep.adapter.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.adapter.intenz.IintenzAdapter;
import uk.ac.ebi.ep.adapter.intenz.IntenzAdapter;
import uk.ac.ebi.ep.adapter.literature.ILiteratureAdapter;
import uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter;
import uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter.LabelledCitation;
import uk.ac.ebi.ep.adapter.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.adapter.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.adapter.reactome.ReactomeServiceException;
import uk.ac.ebi.ep.adapter.rhea.IRheaAdapter;
import uk.ac.ebi.ep.adapter.rhea.RheaWsAdapter;
import uk.ac.ebi.ep.adapter.uniprot.UniprotWsException;
import uk.ac.ebi.ep.biomart.adapter.BiomartAdapter;
import uk.ac.ebi.ep.biomart.adapter.BiomartFetchDataException;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Entity;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.XRef;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.response.cmlreact.Reaction;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeRetriever extends EnzymeFinder implements IEnzymeRetriever {

//********************************* VARIABLES ********************************//
    private static final Logger LOGGER = Logger.getLogger(EnzymeRetriever.class);
    protected IRheaAdapter rheaAdapter;
    private IReactomeAdapter reactomeAdapter;
    protected IChebiAdapter chebiAdapter;
    protected IDASFeaturesAdapter pdbeAdapter;
    protected ILiteratureAdapter litAdapter;
    protected BiomartAdapter biomartAdapter;
    private IBioportalAdapter bioportalAdapter;

//******************************** CONSTRUCTORS ******************************//
    public EnzymeRetriever(Config searchConfig) {
        super(searchConfig);
        rheaAdapter = new RheaWsAdapter();
        biomartAdapter = new BiomartAdapter();
        bioportalAdapter = new BioportalWsAdapter();
        try {
            pdbeAdapter = new SimpleDASFeaturesAdapter(IDASFeaturesAdapter.PDBE_DAS_URL);
        } catch (Exception e) {
            LOGGER.error("Unable to create a PDBe adapter", e);
        }
        litAdapter = new SimpleLiteratureAdapter();
    }

    /**
     * Lazily constructs a new adapter if needed.
     * @return a Reactome adapter.
     */
    public IReactomeAdapter getReactomeAdapter() {
        if (reactomeAdapter == null) {
            reactomeAdapter = new ReactomeAdapter();
        }
        return reactomeAdapter;
    }

    /**
     * Lazily constructs a new adapter if needed.
     * @return a ChEBI adapter.
     */
    public IChebiAdapter getChebiAdapter() {
        if (chebiAdapter == null) {
            chebiAdapter = new ChebiAdapter();
        }
        return chebiAdapter;
    }

    public EnzymeModel getEnzyme(String uniprotAccession)
            throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = null;
        try {
            enzymeModel = (EnzymeModel) uniprotAdapter.getEnzymeSummary(uniprotAccession);
            intenzAdapter.getEnzymeDetails(enzymeModel);
            List<String> prov = new LinkedList<String>();
            prov.add("IntEnz");
            prov.add("UniProt");
            prov.add("IntEnz - (Integrated relational Enzyme database) is a freely available resource focused on enzyme nomenclature.\n");
            prov.add("UniProt - The mission of UniProt is to provide the scientific community with a comprehensive, high-quality and freely accessible resource of protein sequence and functional information");

            enzymeModel.getEnzyme().setProvenance(prov);
        } catch (UniprotWsException e) {
            throw new EnzymeRetrieverException(
                    "Unable to retrieve from UniProt: " + uniprotAccession);
        } catch (MultiThreadingException ex) {
            throw new EnzymeRetrieverException(
                    "Unable to retrieve entry details! " + uniprotAccession, ex);
        }
        return enzymeModel;
    }

    /**
     * Searches Rhea for the primary UniProt accession in the model and adds the
     * corresponding reactions if found.
     * <br><b>WARNING:</b> the added reactions have links only to Reactome and
     * MACiE. The links are strings containing a complete URL.
     * @param enzymeModel
     * @return the same model updated with ReactionPathway objects, one per
     * 		reaction found.
     * @throws EnzymeRetrieverException
     */
    private EnzymeModel queryRheaWsForReactions(EnzymeModel enzymeModel)
            throws EnzymeRetrieverException {
        List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
        List<Reaction> reactions;
        try {
            reactions = rheaAdapter.getRheasInCmlreact(enzymeModel
            		.getUniprotaccessions().get(0));
        } catch (RheaFetchDataException ex) {
            throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
        }
        for (Reaction reaction : reactions) {
            // XXX This adapted reaction will have links only to Reactome and MACiE!:
            ReactionPathway reactionPathway = rheaAdapter.getReactionPathway(reaction);
            reactionPathways.add(reactionPathway);
        }
        enzymeModel.getReactionpathway().addAll(reactionPathways);
        return enzymeModel;

    }

    private Map<String, String> getReactomeAccQueriedFromUniprot(List<Pathway> reactomeUniprotLinks) {
        Map<String, String> idNameMap = new HashMap<String, String>();
        for (Entity pathway : reactomeUniprotLinks) {
            idNameMap.put(pathway.getId(), pathway.getName());

        }
        return idNameMap;
    }

    public EnzymeModel getReactionsPathways(String uniprotAccession)
            throws EnzymeRetrieverException {
        //Get pathways from uniprot --> maybe not for now
        //Get pathways from Biomart (from Reactome reaction retrieved from Rhea)
        //Choose 2 top pathways to extract from Reactome Website
        // View pathway in reactome should be associated with the reaction.        
        //EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getReactionPathwaySummary(uniprotAccession);
        LOGGER.debug(" -RP- before uniprotAdapter.getEnzymeSummary");
        EnzymeModel enzymeModel = null;
        try {
            enzymeModel = (EnzymeModel) uniprotAdapter.getEnzymeSummary(uniprotAccession);
        } catch (UniprotWsException e) {
            throw new EnzymeRetrieverException(
                    "Unable to get enzyme model for " + uniprotAccession, e);
        }
        // The model comes with Reactome IDs in one ReactionPathway object, no more.
        // Now we get more ReactionPathways (one per Rhea reaction):
        LOGGER.debug(" -RP- before queryRheaWsForReactions");
        queryRheaWsForReactions(enzymeModel);
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();

        /**
         * manually add provenance
         */
//        List<String> prov = new LinkedList<String>();
//        prov.add("Reactome");
//        prov.add("Rhea");
//        //prov.add("RELEASED DATE = " + new Date());
//        prov.add("Reactome is an open-source, open access, manually curated and peer-reviewed pathway database");
//        prov.add("Rhea is a freely available, manually annotated database of chemical reactions created in collaboration with the Swiss Institute of Bioinformatics (SIB).All data in Rhea is freely accessible and available for anyone to use.");
        if (!reactionPathways.isEmpty()) {
//            for(ReactionPathway rp : reactionPathways){
//                rp.setProvenance(prov);
//            }
            try {
                LOGGER.debug(" -RP- before DataTypeConverter.getReactionXrefs");
                List<String> reactomeReactionIds =
                        DataTypeConverter.getReactionXrefs(enzymeModel);
                if (reactomeReactionIds.size() > 0) {
                    LOGGER.debug(" -RP- before getReactionPathwaysByRheaResults");
                    getReactionPathwaysByRheaResults(enzymeModel);
                } else { //found Rhea reaction, but not Reactome reaction referenced in Rhea
                    List<String> pathwayReactomeIds = null;
                    try {
                        LOGGER.debug(" -RP- before biomartAdapter.getPathwaysByUniprotAccession");
                        pathwayReactomeIds = biomartAdapter.getPathwaysByUniprotAccession(uniprotAccession);
                    } catch (BiomartFetchDataException ex) {
                        throw new EnzymeRetrieverException(
                                "Failed to get pathway ids from Biomart for uniprot accession " + uniprotAccession, ex);
                    }
                    LOGGER.debug(" -RP- before getPathwayDescFromReactome");
                    List<Pathway> pathways =
                            getPathwayDescFromReactome(pathwayReactomeIds);
                    //To use Rhea reaction without a reference to a Reactome reaction
                    //all pathways found are assigned to the first Rhea reaction
                    reactionPathways.get(0).setPathways(pathways);
                }
            } catch (EnzymeRetrieverException e) {
                LOGGER.warn("Unable to get pathway info", e);
            }
        } else { //No Rhea reactions found, use reactome reaction and pathways
            LOGGER.debug(" -RP- before getReactionPathwaysByUniprotAcc");
            getReactionPathwaysByUniprotAcc(enzymeModel);
        }
        LOGGER.debug(" -RP- before returning");
        return enzymeModel;
    }

    private List<Pathway> getPathwaysByReactomeReactionsId(String reactomeReactionId)
            throws EnzymeRetrieverException {
        List<Pathway> pathways = null;
        List<String> reactomeStableIds = null;// new ArrayList<String>();
        try {
            reactomeStableIds = biomartAdapter.getPathwaysByReactionId(reactomeReactionId); // FIXME THROWS EXC. FOR REACT_21342.1
        } catch (BiomartFetchDataException ex) {
            // throw new EnzymeRetrieverException("Failed to get reactome pathway stable ids "
            // + "from Biomart for Reaction " + reactomeReactionId, ex);
            LOGGER.fatal(ex);
        }
        pathways = getPathwayDescFromReactome(reactomeStableIds);
        return pathways;
    }

    /**
     * Populates an enzyme model with information related to the catalysed
     * reactions: descriptions, any related pathways.
     * @param enzymeModel
     * @return A list of ReactionPathway objects - the ones from the model -
     * 		with updated information. Note that the underlying model (the
     * 		passed parameter) is also updated.
     * @throws EnzymeRetrieverException if there is any problem querying Reactome.
     */
    private List<ReactionPathway> getReactionPathwaysByRheaResults(
            EnzymeModel enzymeModel)
            throws EnzymeRetrieverException {
        try {
            getReactomeAdapter().addReactionDescriptions(enzymeModel);
        } catch (ReactomeServiceException ex) {
            throw new EnzymeRetrieverException(
                    "Failed to get reaction description from Reactome for Rhea reactions", ex);
        }
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        getPathwaysFromRheaXref(reactionPathways);
        return reactionPathways;
    }

    /**
     * Gets pathways for the enzymatic reactions on the base of xrefs from
     * Rhea to Reactome.
     * @param reactionPathways objects containing reactions.
     * @return the same list of ReactionPathway objects with any added pathways.
     * @throws EnzymeRetrieverException
     */
    protected List<ReactionPathway> getPathwaysFromRheaXref(
            List<ReactionPathway> reactionPathways) {
        for (ReactionPathway reactionPathway : reactionPathways) {
            EnzymeReaction reaction = reactionPathway.getReaction();
            List<Object> xrefs = reaction.getXrefs();
            if (xrefs != null) {
                for (Object xref : xrefs) {
                    String x = (String) xref;
                    // Not all of the xrefs are to Reactome:
                    final int idStart = x.indexOf("REACT_");
                    if (idStart == -1) {
                        continue;
                    }
                    // HACK: if xrefs come from Rhea ws as CML, they are complete URLs:
                    if (x.indexOf("http://") == 0) {
                        x = x.substring(idStart);
                    }
                    // If this fails, at least we have the reactions:
                    try {
                        List<Pathway> pathways = getPathwaysByReactomeReactionsId(x);
                        if (pathways != null) {
                        	// Check that the retrieved pathway IDs
                        	// are not there already:
                        	List<Pathway> newPathways = new ArrayList<Pathway>();
                        	for (Pathway pathway : pathways) {
                        		boolean exists = false;
								for (Pathway existingPathway : reactionPathway.getPathways()) {
									if (existingPathway.getId().equals(pathway.getId())){
										exists = true;
										break;
									}
								}
								if (!exists) newPathways.add(pathway);
							}
                            reactionPathway.getPathways().addAll(newPathways);
                        }
                    } catch (EnzymeRetrieverException e) {
                        LOGGER.error("Unable to retrieve pathways for " + x, e);
                    }
                }
            }
        }
        return reactionPathways;
    }

    private List<ReactionPathway> getReactionPathwaysByUniprotAcc(
            EnzymeModel enzymeModel)
            throws EnzymeRetrieverException {
        //No Rhea reaction, use Reactome reactions
        String uniprotAccession = enzymeModel.getUniprotaccessions().get(0);
        List<ReactionPathway> reactionPathwaysFromReactome = null;
        try {
            reactionPathwaysFromReactome = biomartAdapter.getReactionsByUniprotAccession(uniprotAccession);
        } catch (BiomartFetchDataException ex) {
            throw new EnzymeRetrieverException("Failed to get reactome reactions "
                    + "from Biomart for uniprot accession " + uniprotAccession, ex);
        }
        enzymeModel.setReactionpathway(reactionPathwaysFromReactome);
        try {
            getReactomeAdapter().addReactionDescriptions(enzymeModel);
        } catch (ReactomeServiceException e) {
            LOGGER.error("Unable to retrieve reaction descriptions", e);
        }
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        getPathwaysFromRheaXref(reactionPathways);
        return reactionPathways;
    }

    private List<Pathway> getPathwayDescFromReactome(List<String> reactomeStableIds)
            throws EnzymeRetrieverException {
        List<Pathway> pathways = null;
        /*
         * Ajaxified: disabled until Reactome provides a performant ws.
        
        try {
        pathways = reactomeAdapter.getPathways(reactomeStableIds);
        } catch (ReactomeServiceException ex) {
        throw new EnzymeRetrieverException("Failed to get reactome description "
        + "from Reactome for pathways " + reactomeStableIds, ex);
        }
        
         * For now, we build 'empty' Pathway objects only with their
         * ids, which will be retrieved using ajax from the JSP.
         */
        if (reactomeStableIds != null) {
            for (String id : reactomeStableIds) {
                if (pathways == null) {
                    pathways = new ArrayList<Pathway>();
                }
                Pathway pathway = new Pathway();
                pathway.setId(id);
                pathways.add(pathway);
            }
        }
        return pathways;
    }

    public EnzymeModel getMolecules(String uniprotAccession)
            throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = null;
        try {
            LOGGER.debug("MOLECULES before getting model");
            enzymeModel = (EnzymeModel) uniprotAdapter.getEnzymeSummaryWithMolecules(uniprotAccession);
            // The model contains now only drugs, activators and inhibitors.
            if (megaMapperConnection != null) {
            	// Get bioactive compounds from ChEMBL:
                // Search the mega-map for xrefs from UniProt to ChEMBL:
                LOGGER.debug("MOLECULES before getting xrefs to ChEMBL");
                //this is the total number of CheMBL molecules found
                int totalFound = megaMapperConnection.getMegaMapper().getXrefsSize(
                        MmDatabase.UniProt, uniprotAccession, MmDatabase.ChEMBL);
                List<XRef> chemblXrefs = megaMapperConnection.getMegaMapper().getChMBLXrefs(
                        MmDatabase.UniProt, uniprotAccession, MmDatabase.ChEMBL);
                           LOGGER.debug("MOLECULES before getting ChEMBL molecules");
                if (chemblXrefs != null) {
                    Collection<Molecule> chemblDrugs = new ArrayList<Molecule>();
                    for (XRef chemblXref : chemblXrefs) {
                        Molecule chemblDrug = new Molecule().withId(chemblXref.getToEntry().getEntryId()).withName(chemblXref.getToEntry().getEntryName());
                        chemblDrugs.add(chemblDrug);
                    }
                    enzymeModel.getMolecule().withBioactiveLigands(chemblDrugs);
                    enzymeModel.getMolecule().setTotalFound(totalFound);
                }
            }
            // Get cofactors from IntEnz:
            enzymeModel.getMolecule().withCofactors(
            		intenzAdapter.getCofactors(enzymeModel.getEc()));
            // XXX cofactors are mixed if several EC numbers per UniProt acc
            // XXX wrong cofactors if OR'ed (ex. EC 1.1.1.1 and P07327)
            
            LOGGER.debug("MOLECULES before getting complete entries from ChEBI");
            getChebiAdapter().getMoleculeCompleteEntries(enzymeModel);
            LOGGER.debug("MOLECULES before provenance");
            List<String> prov = new LinkedList<String>();
            prov.add("ChEBI");
            prov.add("ChEMBL");
            // prov.add("RELEASED DATE = " + new Date());
            prov.add("ChEBI - (Chemical Entities of Biological Interest) is a freely available dictionary of molecular entities focused on ‘small’ chemical compounds.");
            prov.add("ChEMBL is a database of bioactive drug-like small"
                    + " molecules, it contains 2-D structures, calculated"
                    + " properties (e.g. logP, Molecular Weight, Lipinski"
                    + " Parameters, etc.) and abstracted bioactivities (e.g."
                    + " binding constants, pharmacology and ADMET data).");
            if (enzymeModel.getMolecule() != null) {
                enzymeModel.getMolecule().setProvenance(prov);
            }
        } catch (ChebiFetchDataException ex) {
            throw new EnzymeRetrieverException(
                    "Failed to get small molecule details from Chebi", ex);
        } catch (UniprotWsException e) {
            throw new EnzymeRetrieverException(
                    "Unable to get enzyme summary for " + uniprotAccession, e);
        }
        return enzymeModel;
    }

    public EnzymeModel getProteinStructure(String uniprotAccession)
            throws EnzymeRetrieverException {
        LOGGER.debug(" -STR- before getEnzymeSummary");
        EnzymeModel enzymeModel = null;
        try {
            enzymeModel = (EnzymeModel) uniprotAdapter.getEnzymeSummary(uniprotAccession);
            ParamOfGetResults params = new ParamOfGetResults(
                    Domains.pdbe.name(), uniprotAccession,
                    FieldsOfPdbe.asStrings());
            List<List<String>> fields = ebeyeAdapter.getFields(params);
            if (fields == null) {
                if (!enzymeModel.getPdbeaccession().isEmpty()) {
                    // Keep any structures coming from UniProt web services:
                    // This may happen because of mismatch UniProt-PDB xrefs.
                    for (String pdbId : enzymeModel.getPdbeaccession()) {
                        ProteinStructure str = new ProteinStructure();
                        str.setId(pdbId);
                        str.setName(pdbId);
                        enzymeModel.getProteinstructure().add(str);
                    }
                    LOGGER.warn("No PDB IDs from EB-Eye,"
                            + " using those from UniProt " + uniprotAccession);
                }
            } else {
                for (int i = 0; i < fields.size(); i++) {
                    ProteinStructure str = new ProteinStructure();
                    str.setId(fields.get(i).get(0));
                    str.setName(fields.get(i).get(1));
                    enzymeModel.getProteinstructure().add(str);
                }
                if (enzymeModel.getPdbeaccession().size() != fields.size()) {
                    LOGGER.warn("Different number of PDB IDs from EB-Eye and"
                            + " UniProt for " + uniprotAccession);
                }
            }
        } catch (UniprotWsException e) {
            throw new EnzymeRetrieverException(
                    "Unable to get enzyme summary for " + uniprotAccession, e);
        }
        /*
        if (pdbeAdapter != null) try {
        List<String> pdbIds = enzymeModel.getPdbeaccession();
        LOGGER.debug(" -STR- before getSegments");
        Collection<SegmentAdapter> segments = pdbeAdapter.getSegments(pdbIds);
        for (SegmentAdapter segment : segments){
        LOGGER.debug(" -STR- before adding structure");
        ProteinStructure structure = new ProteinStructure();
        structure.setId(segment.getId());
        for (FeatureAdapter feature : segment.getFeature()){
        if (feature.getType().getId().equals("description")){
        structure.setDescription(feature.getNotes().get(0)); // FIXME?
        } else if (feature.getType().getId().equals("image")){
        Image image = new Image();
        image.setSource(feature.getLinks().get(0).getHref());
        image.setCaption(feature.getLinks().get(0).getContent());
        image.setHref(feature.getLinks().get(1).getHref());
        structure.setImage(image);
        } else if (feature.getType().getId().equals("provenance")){
        structure.setProvenance(feature.getNotes());
        } else if (feature.getType().getId().equals("summary")){
        DASSummary summary = new DASSummary();
        summary.setLabel(feature.getLabel());
        summary.setNote(feature.getNotes());
        structure.getSummary().add(summary);
        }
        }
        enzymeModel.getProteinstructure().add(structure);
        }
        } catch (MalformedURLException e) {
        throw new EnzymeRetrieverException("Wrong URL", e);
        } catch (JAXBException e) {
        throw new EnzymeRetrieverException("Unable to get data from DAS server", e);
        } catch (ValidationException e){
        throw new EnzymeRetrieverException("Validation error for DASGGF", e);
        }
         */
        LOGGER.debug(" -STR- before returning");
        return enzymeModel;
    }

    public EnzymeModel getDiseases(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = null;
        uk.ac.ebi.ep.enzyme.model.Disease disease = null;
        List<uk.ac.ebi.ep.enzyme.model.Disease> diseaseList = new LinkedList<Disease>();

        try {
            enzymeModel = (EnzymeModel) uniprotAdapter.getEnzymeSummary(uniprotAccession);
            intenzAdapter.getEnzymeDetails(enzymeModel);
            Collection<XRef> xRefList = megaMapperConnection.getMegaMapper().getXrefs(MmDatabase.UniProt, uniprotAccession, MmDatabase.EFO, MmDatabase.OMIM, MmDatabase.MeSH);
            if (xRefList != null) {
                for (XRef ref : xRefList) {
                    Entry entry = ref.getToEntry();
                    String efo_id = entry.getEntryId();
                    try {
                        disease = (uk.ac.ebi.ep.enzyme.model.Disease) bioportalAdapter.getDiseaseByName(efo_id);
                        if (disease != null) {

                            for (Disease d : enzymeModel.getDisease()) {
                                disease.getEvidence().add(d.getDescription());
                            }

                            diseaseList.add(disease);
                            enzymeModel.setDisease(diseaseList);
                        }
                    } catch (BioportalAdapterException ex) {
                        LOGGER.error("Error while getting disease using BioPortal adapter", ex);
                    }

                }
            }



        } catch (UniprotWsException ex) {
            LOGGER.error("Error while getting EnzymeSummary from Uniprot Adapter", ex);
        } catch (MultiThreadingException ex) {
            LOGGER.error("Multithreading exception", ex);
        }
        return enzymeModel;
    }

    public EnzymeModel getLiterature(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        List<LabelledCitation> citations =
                litAdapter.getCitations(uniprotAccession, enzymeModel.getPdbeaccession());
        enzymeModel.setLiterature(new ArrayList<Object>(citations)); // FIXME and also the schema!           
        return enzymeModel;
    }
}
