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
import uk.ac.ebi.ep.adapter.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.adapter.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.adapter.reactome.ReactomeServiceException;
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
    protected IReactomeAdapter reactomeAdapter;
    protected IChebiAdapter chebiAdapter;
    protected IDASFeaturesAdapter pdbeAdapter;
    protected ILiteratureAdapter litAdapter;
    protected BiomartAdapter biomartAdapter;

//******************************** CONSTRUCTORS ******************************//
    public EnzymeRetriever(Config searchConfig) {
    	super(searchConfig);
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

    public IReactomeAdapter getReactomeAdapter() {
		return reactomeAdapter;
	}

	public EnzymeModel getEnzyme(String uniprotAccession)
	throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel)
        		uniprotAdapter.getEnzymeSummary(uniprotAccession);
        try {
            intenzAdapter.getEnzymeDetails(enzymeModel);
        } catch (MultiThreadingException ex) {
            throw new EnzymeRetrieverException("Unable to retrieve the entry details! ", ex);
        }
        return enzymeModel;
    }

    /**
     * Searches Rhea for any EC numbers in the model and adds the corresponding
     * reactions if found.
     * <br><b>WARNING:</b> the added reactions have links only to Reactome and
     * MACiE.
     * @param enzymeModel
     * @return the same model updated with ReactionPathway objects, one per
     * 		reaction found.
     * @throws EnzymeRetrieverException
     */
    public EnzymeModel queryRheaWsForReactions(EnzymeModel enzymeModel)
	throws EnzymeRetrieverException {
        List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
        List<String> ecList = enzymeModel.getEc();
        String query = LuceneQueryBuilder.createQueryIN(
        		IRheaAdapter.RheaQueryFields.EC.name(), false, ecList);
        List<Reaction> reactions;
        try {
            reactions = rheaAdapter.getRheasInCmlreact(query);
        } catch (RheaFetchDataException ex) {
            throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
        }
        for (Reaction reaction : reactions) {
        	// This adapted reaction will have links only to Reactome and MACiE!:
            ReactionPathway reactionPathway = rheaAdapter.getReactionPathway(reaction);
            reactionPathways.add(reactionPathway);
        }
        enzymeModel.getReactionpathway().addAll(reactionPathways);
        return enzymeModel;

    }

    public Map<String,String> getReactomeAccQueriedFromUniprot(List<Pathway> reactomeUniprotLinks ) {
        Map<String, String> idNameMap = new HashMap<String, String>();
        for (Entity pathway:reactomeUniprotLinks) {
            idNameMap.put(pathway.getId(),pathway.getName());

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
/* Future fix TODO        
        EnzymeModel enzymeModel = (EnzymeModel)
        		uniprotAdapter.getEnzymeSummaryWithReactionPathway(uniprotAccession);
*/
    	LOGGER.debug(" -RP- before uniprotAdapter.getEnzymeSummary");
        EnzymeModel enzymeModel = (EnzymeModel)
        		uniprotAdapter.getEnzymeSummary(uniprotAccession);
        // The model comes with Reactome IDs in one ReactionPathway object, no more.
        // Now we get more ReactionPathways (one per Rhea reaction):
    	LOGGER.debug(" -RP- before queryRheaWsForReactions");
        queryRheaWsForReactions(enzymeModel);
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        if (!reactionPathways.isEmpty()) {
/* Future fix TODO
        	for (ReactionPathway reactionPathway : reactionPathways) {
        		// These are Reactome IDs from UniProt WS:
				List<Pathway> pathways = reactionPathway.getPathways();
				if (pathways != null && !pathways.isEmpty()){
					// Populate with extra info:
					// TODO
				}
			}
*/
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
                            "Failed to get pathway ids from Biomart for uniprot accession " +uniprotAccession, ex);
                }
            	LOGGER.debug(" -RP- before getPathwayDescFromReactome");
                List<Pathway> pathways = getPathwayDescFromReactome(pathwayReactomeIds);
                //To use Rhea reaction without a reference to a Reactome reaction
                //all pathways found are assigned to the first Rhea reaction
                reactionPathways.get(0).setPathways(pathways);
            }
            
        } else { //No Rhea reactions found, use reactome reaction and pathways
        	LOGGER.debug(" -RP- before getReactionPathwaysByUniprotAcc");
            getReactionPathwaysByUniprotAcc(enzymeModel);
        }
    	LOGGER.debug(" -RP- before returning");
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

    /**
     * Populates an enzyme model with information related to the catalysed
     * reactions: descriptions, any related pathways.
     * @param enzymeModel
     * @return A list of ReactionPathway objects - the ones from the model -
     * 		with updated information. Note that the underlying model (the
     * 		passed parameter) is also updated.
     * @throws EnzymeRetrieverException
     */
    private List<ReactionPathway> getReactionPathwaysByRheaResults(
    		EnzymeModel enzymeModel)
	throws EnzymeRetrieverException {
        try {
            reactomeAdapter.addReactionDescriptions(enzymeModel);
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
    public List<ReactionPathway> getPathwaysFromRheaXref(
    		List<ReactionPathway> reactionPathways)
	throws EnzymeRetrieverException {
        for (ReactionPathway reactionPathway : reactionPathways) {
            EnzymeReaction reaction = reactionPathway.getReaction();                
            List<Object> xrefs = reaction.getXrefs();
            if (xrefs != null) {
            	// FIXME: why should the first one be Reactome's??
                String reactomeReactionId = (String)xrefs.get(0);
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
    	for (String id: reactomeStableIds) {
    		if (pathways == null) pathways = new ArrayList<Pathway>();
			Pathway pathway = new Pathway();
			pathway.setId(id);
			pathways.add(pathway);
		}
        return pathways;
    }

    public EnzymeModel getMolecules(String uniprotAccession)
	throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel)
        		uniprotAdapter.getEnzymeSummaryWithMolecules(uniprotAccession);
        try {
            chebiAdapter.getMoleculeCompleteEntries(enzymeModel);
        } catch (ChebiFetchDataException ex) {
            throw new EnzymeRetrieverException("Failed to get small molecule details from Chebi", ex);
        }
        return enzymeModel;
    }

    public EnzymeModel getProteinStructure(String uniprotAccession)
	throws EnzymeRetrieverException {
        //EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getEnzymeSummaryWithProteinStructure(uniprotAccession);

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
