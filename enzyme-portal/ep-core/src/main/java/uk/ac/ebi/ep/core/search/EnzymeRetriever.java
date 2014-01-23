package uk.ac.ebi.ep.core.search;

import java.util.*;

import org.apache.log4j.Logger;
import org.xml_cml.schema.cml2.react.Reaction;

import uk.ac.ebi.ep.adapter.bioportal.BioportalAdapterException;
import uk.ac.ebi.ep.adapter.bioportal.BioportalWsAdapter;
import uk.ac.ebi.ep.adapter.bioportal.IBioportalAdapter;
import uk.ac.ebi.ep.adapter.chebi.ChebiAdapter;
import uk.ac.ebi.ep.adapter.chebi.ChebiFetchDataException;
import uk.ac.ebi.ep.adapter.chebi.IChebiAdapter;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;
import uk.ac.ebi.ep.adapter.das.SimpleDASFeaturesAdapter;
import uk.ac.ebi.ep.adapter.ebeye.Domains;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.FieldsOfPdbe;
import uk.ac.ebi.ep.adapter.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.adapter.literature.ILiteratureAdapter;
import uk.ac.ebi.ep.adapter.literature.LabelledCitation;
import uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter;
import uk.ac.ebi.ep.adapter.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.adapter.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.adapter.reactome.ReactomeServiceException;
import uk.ac.ebi.ep.adapter.rhea.IRheaAdapter;
import uk.ac.ebi.ep.adapter.rhea.RheaWsAdapter;
import uk.ac.ebi.ep.adapter.uniprot.UniprotWsException;
import uk.ac.ebi.ep.biomart.adapter.BiomartAdapter;
import uk.ac.ebi.ep.biomart.adapter.BiomartFetchDataException;
import uk.ac.ebi.ep.core.DiseaseComparator;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.*;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 *
 * @since 1.0
 * @version $LastChangedRevision$ <br/> $LastChangedDate$ <br/> $Author$
 * @author $Author$
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
    }

    public IBioportalAdapter getBioportalAdapter() {
        return bioportalAdapter;
    }


    

    /**
     * Lazily constructs a new adapter if needed.
     *
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
     *
     * @return a ChEBI adapter.
     */
    public IChebiAdapter getChebiAdapter() {
        if (chebiAdapter == null) {
            chebiAdapter = new ChebiAdapter();
        }
        return chebiAdapter;
    }

    public ILiteratureAdapter getLiteratureAdapter() {
        if (litAdapter == null) {
            litAdapter = new SimpleLiteratureAdapter();
        }
        return litAdapter;
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
     * corresponding reactions if found. <br><b>WARNING:</b> the added reactions
     * have links only to Reactome and MACiE. The links are strings containing a
     * complete URL.
     *
     * @param enzymeModel
     * @return the same model updated with ReactionPathway objects, one per
     * reaction found.
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
        // The model comes with any available Reactome pathway IDs
        // in one ReactionPathway object, no more.
        // Now we get more ReactionPathways (one per Rhea reaction):
        LOGGER.debug(" -RP- before queryRheaWsForReactions");
        queryRheaWsForReactions(enzymeModel);
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();

        /*
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
     *
     * @param enzymeModel
     * @return A list of ReactionPathway objects - the ones from the model -
     * with updated information. Note that the underlying model (the passed
     * parameter) is also updated.
     * @throws EnzymeRetrieverException if there is any problem querying
     * Reactome.
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
     * Gets pathways for the enzymatic reactions on the base of xrefs from Rhea
     * to Reactome.
     *
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
                                    if (existingPathway.getId().equals(pathway.getId())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    newPathways.add(pathway);
                                }
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
            enzymeModel = (EnzymeModel)
                    uniprotAdapter.getEnzymeSummary(uniprotAccession);
            addMolecules(enzymeModel);
        } catch (UniprotWsException e) {
            throw new EnzymeRetrieverException(
                    "Unable to get enzyme summary for " + uniprotAccession, e);
        }
        return enzymeModel;
    }

    /**
     * Adds any available data about small molecules to the model.
     * @param enzymeModel the model
     * @throws EnzymeRetrieverException in case of problem retrieving detailed
     *      info about small molecules from ChEBI.
     * @since 1.1.0
     */
    protected void addMolecules(EnzymeModel enzymeModel)
    throws EnzymeRetrieverException {
        try {
            Collection<Compound> compounds = megaMapperConnection
                    .getMegaMapper().getCompounds(enzymeModel.getUniprotid());
            CountableMolecules activators = null, inhibitors = null,
                    cofactors = null, drugs = null, bioactive = null;
            if (compounds != null) for (Compound compound : compounds) {
                // Classify compounds from the mega-map:
                switch(compound.getRole()){
                    case ACTIVATOR:
                        activators = addMoleculeToGroup(activators, compound);
                        break;
                    case INHIBITOR:
                        inhibitors = addMoleculeToGroup(inhibitors, compound);
                        break;
                    case COFACTOR:
                        cofactors = addMoleculeToGroup(cofactors, compound);
                        break;
                    case DRUG:
                        drugs = addMoleculeToGroup(drugs, compound);
                        break;
                    case BIOACTIVE:
                        bioactive = addMoleculeToGroup(bioactive, compound);
                        break;
                }
            }
            enzymeModel.setMolecule(new ChemicalEntity()
                    .withActivators(activators)
                    .withInhibitors(inhibitors)
                    .withCofactors(cofactors)
                    .withDrugs(drugs)
                    .withBioactiveLigands(bioactive));

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
        }
    }

    /**
     * Adds a compound to the group (inhibitors, cofactors, etc).<br/>
     * If the group is <code>null</code> it will be created and initialised.
     * <br>
     * It will be modified by adding the passed compound and increasing the
     * total count.
     * @param group a molecules group.
     * @param comp a compound.
     * @return the modified (possibly newly created) group of molecules.
     */
    private CountableMolecules addMoleculeToGroup(CountableMolecules group,
            Compound comp) {
        if (group == null) {
            group = new CountableMolecules();
            group.setMolecule(new ArrayList<Molecule>());
            group.setTotalFound(0);
        }
        Molecule molecule = new Molecule();
        molecule.setName(comp.getName());
        molecule.setId(comp.getId());
        group.getMolecule().add(molecule);
        group.setTotalFound(group.getTotalFound() + 1);
        return group;
    }

    public EnzymeModel getProteinStructure(String uniprotAccession)
            throws EnzymeRetrieverException {
        LOGGER.debug(" -STR- before getEnzymeSummary");
        EnzymeModel enzymeModel = null;
        try {
            enzymeModel = (EnzymeModel)
                    uniprotAdapter.getEnzymeSummary(uniprotAccession);
            addProteinStructures(enzymeModel);
        } catch (UniprotWsException e) {
            throw new EnzymeRetrieverException(
                    "Unable to get enzyme summary for " + uniprotAccession, e);
        }
        LOGGER.debug(" -STR- before returning");
        return enzymeModel;
    }

    /**
     * Adds protein structures to an existing enzyme model. It queries EB-Eye
     * using the primary UniProt accession in order to get PDB identifiers and
     * names. If none is found, any PDB IDs already existing in the model (from
     * UniProt) are kept.
     * @param enzymeModel the model to add protein structures to.
     * @since 1.1.0
     */
    private void addProteinStructures(EnzymeModel enzymeModel) {
        ParamOfGetResults params = new ParamOfGetResults(
                Domains.pdbe.name(),
                enzymeModel.getUniprotaccessions().get(0),
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
                        + " using those from UniProt "
                        + enzymeModel.getUniprotaccessions().get(0));
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
                        + " UniProt for "
                        + enzymeModel.getUniprotaccessions().get(0));
            }
        }
    }

    public EnzymeModel getDiseases(String uniprotAccession)
    throws EnzymeRetrieverException {
        try {
            EnzymeModel enzymeModel = (EnzymeModel)
                    uniprotAdapter.getEnzymeSummary(uniprotAccession);
            addDiseases(enzymeModel);
            return enzymeModel;
        } catch (UniprotWsException ex) {
            throw new EnzymeRetrieverException(
                    "Error while getting EnzymeSummary from Uniprot Adapter", ex);
        }
    }

    /**
     * Adds any related diseases to the enzyme model.
     * @param enzymeModel the model without disease info.
     * @since 1.1.0
     */
    protected void addDiseases(EnzymeModel enzymeModel) {
        List<Disease> diseaseList = new LinkedList<Disease>();
        Set<DiseaseComparator> uniDisease = new TreeSet<DiseaseComparator>();
        Map<String, String> diseaseFromMegaMapper = megaMapperConnection.getMegaMapper()
                .getDiseaseByAccession(MmDatabase.UniProt, enzymeModel.getUniprotaccessions().get(0), MmDatabase.EFO, MmDatabase.MeSH, MmDatabase.OMIM);
        // FIXME this code is unreadable (ex. diseaseMap is not a Map but an Entry)
        for (Map.Entry<String, String> diseaseMap : diseaseFromMegaMapper.entrySet()) {
            if (diseaseMap.getKey() != null && diseaseMap.getValue() != null && !diseaseMap.getValue().equals("") && !diseaseMap.getValue().equals(" ")) {
                try {
                    Disease disease_from_bioportal = bioportalAdapter.getDisease(diseaseMap.getKey());
                    if (disease_from_bioportal != null) {
                        for (Disease d : enzymeModel.getDisease()) {
                            // FIXME adding EVERY disease description
                            // (actually, evidence!)
                            // from model to EVERY BioPortal disease ???
                            disease_from_bioportal.getEvidence().add(d.getDescription());
                            DiseaseComparator dc = new DiseaseComparator(disease_from_bioportal);
                            uniDisease.add(dc);
                        }
                    }
                } catch (BioportalAdapterException ex) {
                    LOGGER.error("Error while getting disease using BioPortal adapter", ex);
                }
            }
        }
        for (DiseaseComparator disease : uniDisease) {
            diseaseList.add(disease.getDisease());
        }
        enzymeModel.setDisease(diseaseList);
    }

    public EnzymeModel getLiterature(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        List<LabelledCitation> citations = getLiteratureAdapter().getCitations(
                uniprotAccession, enzymeModel.getPdbeaccession());
        if (citations != null) {
            enzymeModel.setLiterature(new ArrayList<Object>(citations));
        }

        return enzymeModel;
    }
    
    /**
     * Retrieves the whole enzyme model for comparisons.
     * @param acc the UniProt accession of the enzyme.
     * @return a complete model.
     * @throws EnzymeRetrieverException in case of problem retrieving the model
     *      from UniProt, or small molecules from ChEBI.
     * @since 1.1.0
     */
    public EnzymeModel getWholeModel(String acc)
    throws EnzymeRetrieverException {
        // This model includes summary, reactions and pathways:
        EnzymeModel model = getReactionsPathways(acc);
        // Add the missing bits:
        addProteinStructures(model);
        addMolecules(model);
        addDiseases(model);
        return model;
    }
}
