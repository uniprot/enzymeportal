package uk.ac.ebi.ep.core.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.adapters.features.FeatureAdapter;
import uk.ac.ebi.das.jdas.exceptions.ValidationException;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeServiceException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.ws.client.IRheaAdapter;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.client.RheasResourceClient;
import uk.ac.ebi.rhea.ws.response.cmlreact.Reaction;


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
    protected String accesion;
    protected IRheaAdapter rheaAdapter;
    private static Logger log = Logger.getLogger(EnzymeRetriever.class);

    protected IReactomeAdapter reactomeAdapter;
    public String getAccesion() {
        return accesion;
    }

    public void setAccesion(String accesion) {
        this.accesion = accesion;
    }

//******************************** CONSTRUCTORS ******************************//
    public EnzymeRetriever() {
        rheaAdapter = new RheasResourceClient();
        reactomeAdapter = new ReactomeAdapter();
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

    public EnzymeModel getReactionsPathways(EnzymeModel enzymeModel) throws EnzymeRetrieverException {
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
        for (Pathway pathway:reactomeUniprotLinks) {
            idNameMap.put(pathway.getId(),pathway.getTitle());

        }
        return idNameMap;
    }
    public EnzymeModel getReactionsPathways(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        ReactionPathway reactomeUniproPw= enzymeModel.getReactionpathway().get(0);
        List<Pathway> reactomeUniprotLinks = reactomeUniproPw.getPathways();
        Map<String,String> reactomeAccessionsInUniprot =
                getReactomeAccQueriedFromUniprot(reactomeUniprotLinks);

        //Query Rhea WS
        getReactionsPathways(enzymeModel);
        
        List<ReactionPathway> reactionPathways = reactionPathways = enzymeModel.getReactionpathway();
        //get the reaction and pathways with rhea ws results set
        //List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        for (ReactionPathway reactionPathway: reactionPathways) {
            //List<Pathway> pathways = reactionPathway.getPathways();
            EnzymeReaction enzymeReaction = reactionPathway.getReaction();
           // for (Pathway pathway: pathways) {
            if (enzymeReaction != null) {
                List<Object> rheaReactomeLinks = enzymeReaction.getXrefs();
                String reactomeUrl = null;
                if (rheaReactomeLinks.size() > 0) {
                    reactomeUrl = (String)rheaReactomeLinks.get(0);
                    try {
                        //Object[] results = this.reactomeAdapter.getReactionPathway(reactomeUrl);
                        String reactomeAccession = reactomeUrl.split("=")[1];
                        String reactionDesc = this.reactomeAdapter.getReactionDescription(reactomeAccession);

                        enzymeReaction.setDescription(reactionDesc);
                    }
                    catch (ReactomeServiceException ex) {
                        log.error("Failed to retrieve reaction desciption for " +reactomeUrl, ex);
                    }

                }
            }
        }
        Set<Entry<String, String>> entries = reactomeAccessionsInUniprot.entrySet();
        List<Pathway> pathways = new ArrayList<Pathway>();
        for (Entry<String, String> entry: entries) {
             String reactomeAccession = entry.getKey();
             String pathwayDesc = null;
            try {
                pathwayDesc = this.reactomeAdapter.getPathwayDescription(reactomeAccession);
            } catch (ReactomeServiceException ex) {
                log.error("Failed to retrieve pathway desciption for " +reactomeAccession, ex);
            }
            Pathway pathway = new Pathway();
            pathway.setId(reactomeAccession);
            pathway.setTitle(entry.getValue());
            pathway.setDescription(pathwayDesc);
            String url = Database.REACTOME.getEntryUrl(reactomeAccession);
            pathway.setUrl(url);
            pathways.add(pathway);
            //reactionPathways.add(reactionPathway);
        }
        //ReactionPathway reactionPathway = reactionPathways.get(0);
        //ReactionPathway reactionPathway = null;
        if (reactionPathways.size() == 0) {
            ReactionPathway reactionPathway = new ReactionPathway();            
            reactionPathways.add(reactionPathway);
        }

        reactionPathways.get(0).setPathways(pathways);

        //enzymeModel.setReactionpathway(reactionPathways);

        return enzymeModel;
    }

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
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        List<String> pdbIds = enzymeModel.getPdbeaccession();
    	try {
			Collection<SegmentAdapter> segments = pdbeAdapter.getFeatures(pdbIds);
			// TODO: fill enzymeModel with structure(s)
            for (SegmentAdapter segment : segments){
                try {
                    //String pdbCode = segments.getId();
                    for (FeatureAdapter feature : segment.getFeature()) {
                    }
                } catch (ValidationException ex) {
                    java.util.logging.Logger.getLogger(EnzymeRetriever.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
		} catch (MalformedURLException e) {
	        throw new EnzymeRetrieverException("Wrong URL");
		} catch (JAXBException e) {
	        throw new EnzymeRetrieverException("Unable to get data from DAS server");
		}
    	return enzymeModel;
    }

    
    public EnzymeModel getMolecules(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getDiseases(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getLiterarture(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
