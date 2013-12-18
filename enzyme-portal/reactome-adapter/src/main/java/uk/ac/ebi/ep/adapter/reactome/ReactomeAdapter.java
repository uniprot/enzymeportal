package uk.ac.ebi.ep.adapter.reactome;

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
import uk.ac.ebi.ep.adapter.reactome.ReactomeWsCallable.ReactomeClass;
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
    
    private ReactomeWsCallable reactomeWsCallable;
    
    /**
     * Lazy getter.
     * @return a {@link ReactomeWsCallable} without any Reactome ID set, as
     * 		it will not be used anyway.
     */
    private ReactomeWsCallable getReactomeWsCallable(){
    	if (reactomeWsCallable == null){
    		reactomeWsCallable = new ReactomeWsCallable(config, null);
    	}
    	return reactomeWsCallable;
    }
    
    public String getReactionDescription(String reactomeAccession)
	throws ReactomeServiceException{
        return getReactomeWsCallable().getDescription(
        		ReactomeClass.Reaction, reactomeAccession);
    }

    public String getPathwayDescription(String reactomeAccession)
	throws ReactomeServiceException {
        return getReactomeWsCallable().getDescription(
        		ReactomeClass.Pathway, reactomeAccession);
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
                List<Object> reactionLinks = enzymeReaction.getXrefs();
                String reactomeUrl = null;
                if (reactionLinks.size() > 0) {
                	for (Object reactionLink : reactionLinks) {
						reactomeUrl = (String) reactionLink;
						if (!reactomeUrl.contains("REACT_")) continue;
						// Only Reactome xrefs, as descriptions come from there
	                    try {
	                        final String[] split = reactomeUrl.split("=");
	                        // Some xrefs come as whole URLs, others as plain IDs
							String reactomeAccession = split.length > 1?
									split[1] : split[0];
	                        String reactionDesc = getReactionDescription(reactomeAccession);
	                        enzymeReaction.setDescription(reactionDesc);
	                        // XXX: If more than one xref to Reactome, only first description is taken
	                        break;
	                    }
	                    catch (ReactomeServiceException ex) {
	                        throw new ReactomeServiceException("Failed to retrieve reaction desciption for " +reactomeUrl, ex);
	                    }
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
	            GetPathwayCaller callable = new ReactomeCallable.GetPathwayCaller(id, config);
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
