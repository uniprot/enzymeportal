package uk.ac.ebi.ep.adapter.reactome;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.adapter.reactome.ReactomeCallable.GetPathwayCaller;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 * Proxy to retrieve enzyme information from Reactome.
 * Note that in order to use an instance of this class, a proper
 * configuration must be set.
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeAdapter implements IReactomeAdapter{

	private ReactomeConfig config;

    private final Logger logger = Logger.getLogger(ReactomeAdapter.class);
    
    public String getReactionDescription(String reactomeAccession)
	throws ReactomeServiceException{
        String reactionDesc = null;
        //String searchUrl = IReactomeAdapter.REACTOME_SEARCH_URL +reactomeAccession;
        try {
            reactionDesc =  ReactomeUtil.parseReactomeHtml(reactomeAccession, "Reaction.gif");
        } catch (MalformedURLException ex) {
            throw new ReactomeConnectionException(
                    "Failed to create the URL for " +reactomeAccession, ex);
        } catch (IOException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get reaction and pathway ids from Reactome Web site! ", ex);
        }
        return reactionDesc;
    }

    public String getPathwayDescription(String reactomeAccession)
	throws ReactomeServiceException {
        String pathwayDesc = null;
        //String searchUrl = IReactomeAdapter.REACTOME_SEARCH_URL +reactomeAccession;
        try {
            pathwayDesc =  ReactomeUtil.parseReactomeHtml(reactomeAccession, "Pathway.gif");
        } catch (MalformedURLException ex) {
            throw new ReactomeConnectionException(
                    "Failed to create the URL for " +reactomeAccession, ex);
        } catch (IOException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get pathway id from Reactome Web site! ", ex);
        }
        return pathwayDesc;

    }

    /**
     * {@inheritDoc}
     * This implementation uses any xrefs from reactions, trusting WRONGLY
     * they are pointing to Reactome.
     */
    public EnzymeModel addReactionDescriptions(EnzymeModel enzymeModel)
	throws ReactomeServiceException {
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        //get the reaction and pathways with rhea ws results set
        for (ReactionPathway reactionPathway: reactionPathways) {
            EnzymeReaction enzymeReaction = reactionPathway.getReaction();
           // for (Pathway pathway: pathways) {
            if (enzymeReaction != null) {
                //get Reactome reaction id retrieved from Rhea
            	// FIXME: rhea reactions might come with other xrefs (ex. MACiE)
                List<Object> rheaReactomeLinks = enzymeReaction.getXrefs();
                String reactomeUrl = null;
                if (rheaReactomeLinks.size() > 0) {
                    reactomeUrl = (String)rheaReactomeLinks.get(0);
                    try {
                        String reactomeAccession = reactomeUrl.split("=")[1];
                        String reactionDesc = getReactionDescription(reactomeAccession);
                        enzymeReaction.setDescription(reactionDesc);
                    }
                    catch (ReactomeServiceException ex) {
                        throw new ReactomeServiceException("Failed to retrieve reaction desciption for " +reactomeUrl, ex);
                    }
                }
            }
        }
        return enzymeModel;
    }

    public List<Pathway> getPathways(List<String> stableIds)
	throws ReactomeServiceException {
        ExecutorService pool = Executors.newCachedThreadPool();
	    CompletionService<Pathway> ecs =
	    		new ExecutorCompletionService<Pathway>(pool);
        List<Pathway> pathways = new ArrayList<Pathway>();
        try {
        	logger.debug(" -RP- before sending jobs");
	        for (String id: stableIds) {
	            GetPathwayCaller callable = new ReactomeCallable.GetPathwayCaller(id);
	            ecs.submit(callable);
	        }
        	logger.debug(" -RP- before retrieving jobs");
	        for (int i = 0; i < stableIds.size(); i++) {
				try {
					Future<Pathway> future =
							ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
	    			if (future != null){
		                pathways.add(future.get());
	    			} else {
	    				logger.warn("Job did not return");
	    			}
	            } catch (InterruptedException ex) {
	                throw new ReactomeServiceException("The thread was interrupted while retieving from Reactome pathway " +stableIds.get(i), ex);
	            } catch (ExecutionException ex) {
	                throw new ReactomeServiceException("Failed to execute the thread to retrieve from Reactome pathway " +stableIds.get(i), ex);
	            }
			}
        } finally {
            pool.shutdown();
        }
    	logger.debug(" -RP- before returning");
        return pathways;
    }

	public void setConfig(ReactomeConfig config) {
		this.config = config;
	}


}
