package uk.ac.ebi.ep.reactome;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.reactome.ReactomeCallable.GetPathwayCaller;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeAdapter implements IReactomeAdapter{

//********************************* VARIABLES ********************************//
    //seconds
    public static final int TIME_OUT = 40;

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public String getReactionDescription(String reactomeAccession)throws ReactomeServiceException{
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

/*
    public String getReactionDescription(String reactomeUrl)throws ReactomeServiceException{
        Long[] reactionAndPathwayIds = null;
        //String[] pathwayidAndReactionDesc = new String[2];
        try {
            reactionAndPathwayIds = ReactomeUtil.parseUrl(reactomeUrl);
        } catch (MalformedURLException ex) {
            throw new ReactomeConnectionException(
                    "Failed to create the URL for " +reactomeUrl, ex);
        } catch (IOException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get reaction and pathway ids from Reactome Web site! ", ex);
        }                
        Long reactionId = reactionAndPathwayIds[0];
        Reaction reaction = getReactomeReaction(reactionId);
        */
        /*
        try {
            Pathway pathway = (Pathway) call.invoke(new Object[]{new Long(pathwayAndReactionIds[0])});
        } catch (RemoteException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get the pathway from Reactome WS! ", ex);
        }
         *
         */

        //long pathwayId = reaction.getId();
        //pathwayidAndReactionDesc[0] = String.valueOf(pathwayId);
//        pathwayidAndReactionDesc[1] = ReactomeUtil.getReactionDescription(reaction);
/*
        Summation summation = reaction.getSummation().get(0);
        if (summation.getText() == null) {
            long sumId = summation.getId();
            Call call = this.reactomeService.getQueryByIdCall();
            summation = getSummationById(sumId);
        }
        String reactionDesc = summation.getText();
        return reactionDesc;
    }
*/

    public String getPathwayDescription(String reactomeAccession) throws ReactomeServiceException {
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

    public EnzymeModel getReationDescription(EnzymeModel enzymeModel) throws ReactomeServiceException {
        //To be done in EnzymeRetriever
        //queryRheaWsForReactions(enzymeModel);

        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        //get the reaction and pathways with rhea ws results set
        //List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        for (ReactionPathway reactionPathway: reactionPathways) {
            //List<Pathway> pathways = reactionPathway.getPathways();
            EnzymeReaction enzymeReaction = reactionPathway.getReaction();
           // for (Pathway pathway: pathways) {
            if (enzymeReaction != null) {
                //get Reactome reaction id retrieved from Rhea
                List<Object> rheaReactomeLinks = enzymeReaction.getXrefs();
                String reactomeUrl = null;
                if (rheaReactomeLinks.size() > 0) {
                    reactomeUrl = (String)rheaReactomeLinks.get(0);
                    try {
                        //Object[] results = this.reactomeAdapter.getReactionPathway(reactomeUrl);
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

    public List<Pathway> getPathwayDescription (
            List<String> stableIds) throws ReactomeServiceException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Pathway> pathways = new ArrayList<Pathway>();
        try {            
            for (String id: stableIds) {
                try {
                    GetPathwayCaller callable = new ReactomeCallable.GetPathwayCaller(id);
                    Future<Pathway> future = pool.submit(callable);
                    Pathway pathway = future.get(TIME_OUT, TimeUnit.SECONDS);
                    pathways.add(pathway);
                } catch (InterruptedException ex) {
                    throw new ReactomeServiceException("The thread was interrupted while retieving from Reactome pathway " +id, ex);
                } catch (ExecutionException ex) {
                    throw new ReactomeServiceException("Failed to execute the thread to retrieve from Reactome pathway " +id, ex);
                } catch (TimeoutException ex) {
                    throw new ReactomeServiceException("Time out to retrieve from Reactome pathway " +id, ex);
                }
            }
        }
        finally {
            pool.shutdown();
        }

        return pathways;
    }


}
