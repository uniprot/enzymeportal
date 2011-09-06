package uk.ac.ebi.ep.core.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.adapters.features.FeatureAdapter;
import uk.ac.ebi.das.jdas.exceptions.ValidationException;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;
import uk.ac.ebi.ep.adapter.das.SimpleDASFeaturesAdapter;
import uk.ac.ebi.ep.adapter.literature.ILiteratureAdapter;
import uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter;
import uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter.LabelledCitation;
import uk.ac.ebi.ep.biomart.adapter.BiomartAdapter;
import uk.ac.ebi.ep.biomart.adapter.BiomartFetchDataException;
import uk.ac.ebi.ep.chebi.adapter.ChebiAdapter;
import uk.ac.ebi.ep.chebi.adapter.ChebiFetchDataException;
import uk.ac.ebi.ep.chebi.adapter.IChebiAdapter;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.DASSummary;
import uk.ac.ebi.ep.enzyme.model.Entity;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Image;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeServiceException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.rhea.ws.client.IRheaAdapter;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.client.RheasResourceClient;
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
    private static Logger log = Logger.getLogger(EnzymeRetriever.class);

    protected IReactomeAdapter reactomeAdapter;

    protected IChebiAdapter chebiAdapter;
    protected IDASFeaturesAdapter pdbeAdapter;
    protected ILiteratureAdapter litAdapter;
    protected BiomartAdapter biomartAdapter;

//******************************** CONSTRUCTORS ******************************//
    public EnzymeRetriever() {
        rheaAdapter = new RheasResourceClient();
        reactomeAdapter = new ReactomeAdapter();
        chebiAdapter = new ChebiAdapter();
        biomartAdapter = new BiomartAdapter();
        try {
			pdbeAdapter = new SimpleDASFeaturesAdapter(IDASFeaturesAdapter.PDBE_DAS_URL);
		} catch (Exception e) {
			LOGGER.error("Unable to create a PDBe adapter", e);
		}
        litAdapter = new SimpleLiteratureAdapter();
    }

//****************************** GETTER & SETTER *****************************//
//********************************** METHODS *********************************//
    public EnzymeModel getEnzyme(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel) this.uniprotAdapter.getEnzymeSummary(uniprotAccession);
        try {
            this.intenzAdapter.getEnzymeDetails(enzymeModel);
        } catch (MultiThreadingException ex) {
            throw new EnzymeRetrieverException("Unable to retrieve the entry details! ", ex);
        }
        return enzymeModel;
    }

    public EnzymeModel queryRheaWsForReactions(EnzymeModel enzymeModel) throws EnzymeRetrieverException {
        //List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
        List<String> ecList = enzymeModel.getEc();
        String query = LuceneQueryBuilder.createQueryIN(IRheaAdapter.RheaQueryFields.EC.name(), false, ecList);
        List<Reaction> reactions;
        try {
            reactions = this.rheaAdapter.getRheasInCmlreact(query);
        } catch (RheaFetchDataException ex) {
            throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
        }
        for (Reaction reaction : reactions) {
            ReactionPathway reactionPathway = this.rheaAdapter.getReactionPathway(reaction);
            reactionPathways.add(reactionPathway);
        }
        enzymeModel.setReactionpathway(reactionPathways);
        return enzymeModel;

    }

    public Map<String,String> getReactomeAccQueriedFromUniprot(List<Pathway> reactomeUniprotLinks ) {
        Map<String, String> idNameMap = new HashMap<String, String>();
        for (Entity pathway:reactomeUniprotLinks) {
            idNameMap.put(pathway.getId(),pathway.getName());

        }
        return idNameMap;
    }
    public EnzymeModel getReactionsPathways(String uniprotAccession) throws EnzymeRetrieverException {
        //Get pathways from uniprot --> maybe not for now
        //Get pathways from Biomart (from Reactome reaction retrieved from Rhea)
        //Choose 2 top pathways to extract from Reactome Website
        // View pathway in reactome should be associated with the reaction.        
        //EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getReactionPathwaySummary(uniprotAccession);
        
        //TODO check
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getEnzymeSummary(uniprotAccession);

        //List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();

        //ReactionPathway reactomeUniproPw= reactionPathways.get(0);
        //List<Pathway> reactomeUniprotLinks = reactomeUniproPw.getPathways();
        //Map<String,String> reactomeAccessionsInUniprot =
           //     getReactomeAccQueriedFromUniprot(reactomeUniprotLinks);
        //List<String> reactomeStableIdsFromUniprot = new ArrayList<String>();
        //reactomeStableIdsFromUniprot.addAll(reactomeAccessionsInUniprot.keySet());
        //Query Rhea WS
        queryRheaWsForReactions(enzymeModel);

        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();

        if (reactionPathways.size() > 0) {
            List<String> reactomeReactionIds = DataTypeConverter.getReactionXrefs(enzymeModel);
            if (reactomeReactionIds.size() > 0) {
                getReactionPathwaysByRheaResults(enzymeModel);

            } else { //found Rhea reaction, but not reactome reaction referenced in Rhea
                List<String> pathwayReactomeIds = null;
                try {
                    pathwayReactomeIds = biomartAdapter.getPathwaysByUniprotAccession(uniprotAccession);
                } catch (BiomartFetchDataException ex) {
                    throw new EnzymeRetrieverException(
                            "Failed to get pathway ids from Biomart for uniprot accession " +uniprotAccession, ex);
                }
                List<Pathway> pathways = getPathwayDescFromReactome(pathwayReactomeIds);
                //To use Rhea reaction without a reference to a Reactome reaction
                //all pathways found are assigned to the first Rhea reaction
                reactionPathways.get(0).setPathways(pathways);
            }
            
        } else { //No Rhea reactions found, use reactome reaction and pathways
            getReactionPathwaysByUniprotAcc(enzymeModel);
        }
        
        /*
        for (ReactionPathway reactionPathway: reactionPathways) {
            EnzymeReaction reaction = reactionPathway.getReaction();
            if (reaction != null) {
                List<String> reactomeStableIds = new ArrayList<String>();
                List<Object> reactomeReactionIds = reaction.getXrefs();
                //if there is no Reactome reaction id in Rhea

                for (Object reactomeReactionId:reactomeReactionIds ) {
                    String castedId = null;

                    try {
                        castedId = (String) reactomeReactionId;
                        reactomeStableIds.addAll(biomartAdapter.getPathwaysByReactionId(castedId));
                    } catch (BiomartFetchDataException ex) {
                        throw new EnzymeRetrieverException("Failed to get reactome stable ids " +
                                "from Biomart for Reaction " +castedId, ex);
                    }
                    if (reactomeStableIds.size() == 0) {
                        reactomeReactionIds.addAll(reactomeStableIdsFromUniprot);
                    } else {
                        List<Pathway> pathways = null;
                        try {
                            reactomeAdapter.getPathwayDescription(reactomeStableIds);
                        } catch (ReactomeServiceException ex) {
                            throw new EnzymeRetrieverException("Failed to get reactome description " +
                                "from Reactome for Reaction " +castedId, ex);
                        }

                        reactionPathway.setPathways(pathways);
                    }
                }


            }
        }
        */
        return enzymeModel;
    }

    public List<Pathway> getPathwaysByReactomeReactionsId(String reactomeReactionId) throws EnzymeRetrieverException {
        List<Pathway> pathways = null;
        List<String> reactomeStableIds = null;
        try {
            reactomeStableIds = biomartAdapter.getPathwaysByReactionId(reactomeReactionId);
        } catch (BiomartFetchDataException ex) {
            throw new EnzymeRetrieverException("Failed to get reactome pathway stable ids "
                    + "from Biomart for Reaction " + reactomeReactionId, ex);
        }
        pathways= getPathwayDescFromReactome(reactomeStableIds);
        return pathways;
    }

    public List<ReactionPathway> getReactionPathwaysByRheaResults(EnzymeModel enzymeModel) throws EnzymeRetrieverException {
            try {
                reactomeAdapter.getReationDescription(enzymeModel);
            } catch (ReactomeServiceException ex) {
                throw new EnzymeRetrieverException(
                        "Failed to get reaction description from Reactome for Rhea reactions", ex);
            }
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        getPathwaysFromRheaXref(reactionPathways);
        return reactionPathways;
    }

    public List<ReactionPathway> getPathwaysFromRheaXref( List<ReactionPathway> reactionPathways) throws EnzymeRetrieverException {
        //List<String> reactomeReactionId = new ArrayList<String>();
            for (ReactionPathway reactionPathway : reactionPathways) {
                EnzymeReaction reaction = reactionPathway.getReaction();                
                List xref = reaction.getXrefs();
                if (xref != null) {
                    String reactomeReactionId = (String)xref.get(0);
                     List<Pathway> pathways = getPathwaysByReactomeReactionsId(reactomeReactionId);
                    reactionPathway.setPathways(pathways);
                }
            }                   

        return reactionPathways;
    }

    public List<ReactionPathway> getReactionPathwaysByUniprotAcc(EnzymeModel enzymeModel) throws EnzymeRetrieverException {
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
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        getPathwaysFromRheaXref(reactionPathways);
        return reactionPathways;
    }

    public List<Pathway> getPathwayDescFromReactome(List<String> reactomeStableIds) throws EnzymeRetrieverException {
        List<Pathway> pathways = null;
        try {
            pathways = reactomeAdapter.getPathwayDescription(reactomeStableIds);
        } catch (ReactomeServiceException ex) {
            throw new EnzymeRetrieverException("Failed to get reactome description "
                    + "from Reactome for pathways " + reactomeStableIds, ex);
        }
        return pathways;
    }

    public EnzymeModel getMolecules(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getMoleculeSummary(uniprotAccession);
        try {
            this.chebiAdapter.getMoleculeCompleteEntries(enzymeModel);
        } catch (ChebiFetchDataException ex) {
            throw new EnzymeRetrieverException("Failed to get small molecule details from Chebi", ex);
        }
        return enzymeModel;

    }
/*
    public EnzymeModel getMolecules(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getMoleculeSummary(uniprotAccession);
        List<Molecule> drugs = enzymeModel.getMolecule().getDrugs();
        List<Molecule> moleculesFromChebi = new ArrayList<Molecule>();
        for (Molecule drug:drugs) {
            List<Object> drugBankIds = drug.getXrefs();
            String drugBankId = null;
            if (drugBankIds.size() > 0 ) {
                drugBankId = (String) drugBankIds.get(0);
            }
            if (drugBankId != null) {
                try {
                    List<String> chebiIdsLinkedToDrugbank = this.chebiAdapter.getChebiLiteEntity(drugBankId);
                    //System.out.print("Chebi drugs: " +chebiEntitiesLinkedToDrugBank);
                    for (String chebiId: chebiIdsLinkedToDrugbank) {
                        Molecule moleculeFromChebi = (Molecule)this.chebiAdapter.getEpChemicalEntity(chebiId);
                        moleculeFromChebi.setXrefs(drugBankIds);
                        moleculesFromChebi.add(moleculeFromChebi);
                    }
                } catch (ChebiFetchDataException ex) {
                    log.error("Failed to query Chebi for DrugBank id " +drugBankId, ex);
                    //java.util.logging.Logger.getLogger(EnzymeRetriever.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        enzymeModel.getMolecule().setDrugs(moleculesFromChebi);
        return enzymeModel;

    }
*/
/*
    public EnzymeModel getReactionsPathways(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        getReactionsPathways(enzymeModel);
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        for (ReactionPathway reactionPathway: reactionPathways) {
            List<Pathway> pathways = reactionPathway.getPathways();
            EnzymeReaction enzymeReaction = reactionPathway.getReaction();
            for (Pathway pathway: pathways) {
                String reactomeUrl = (String)pathway.getUrl();
                try {
                    String[] result = this.reactomeAdapter.getReaction(reactomeUrl);
                    if (result.length > 1){
                        enzymeReaction.setDescription(result[1]);
                    }
                } catch (ReactomeServiceException ex) {
                    log.error("Failed to retrieve reaction desciption for " +reactomeUrl);
                }
            }
        }        
        return enzymeModel;
    }
*/

    public EnzymeModel getProteinStructure(String uniprotAccession)
	throws EnzymeRetrieverException {
        //EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getProteinStructureSummary(uniprotAccession);

    	if (pdbeAdapter != null) try {
            List<String> pdbIds = enzymeModel.getPdbeaccession();
			Collection<SegmentAdapter> segments = pdbeAdapter.getSegments(pdbIds);
            for (SegmentAdapter segment : segments){
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
    	return enzymeModel;
    }

    public EnzymeModel getDiseases(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getLiterature(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        List<LabelledCitation> citations =
        		litAdapter.getCitations(uniprotAccession, enzymeModel.getPdbeaccession());
        enzymeModel.setLiterature(new ArrayList<Object>(citations)); // FIXME and also the schema!
        return enzymeModel;
    }
}
