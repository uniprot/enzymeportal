package uk.ac.ebi.ep.reactome;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.apache.axis.client.Call;
import org.reactome.cabig.domain.Event;
import org.reactome.cabig.domain.Pathway;
import org.reactome.cabig.domain.Reaction;
import org.reactome.cabig.domain.Summation;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

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
    protected ReactomeServiceConfig reactomeService;

    public ReactomeAdapter() {
        reactomeService = new ReactomeServiceConfig();
    }
//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static void main(String[] args) {
        ReactomeServiceConfig serviceInitializer = new ReactomeServiceConfig();
        //Call diagramCall = serviceInitializer.generatePathwayDiagramInSVGNewCall();
        //Call queryById = serviceInitializer.getQueryByIdCall();
        ReactomeAdapter reactomeAdapter = new ReactomeAdapter();

        try {            
            //testQueryPathwaysForReferenceEntities(getRefCall);
            //generatePathwayDiagramInSVG(diagramCall);
            //reactomeAdapter.parseUrl();
            //reactomeAdapter.getReaction("http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_6763.1");
            Object[] results = reactomeAdapter.getReactionPathway("http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_6763.1");
            System.out.print(results);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public Object[] getReactionAndPathwayByIds(Long[] ids) throws Exception {
        Call call = this.reactomeService.getQueryByObjectsCall();
        //Object[] eventObjects = (Object[]) call.invoke(new Object[]{ids});
        List<Object> objects = new ArrayList<Object>();
        Pathway pathway = new Pathway();
        pathway.setId(ids[1]);
        objects.add(pathway);
        Reaction reaction = new Reaction();
        reaction.setId(ids[0]);
        objects.add(reaction);
        Object[] eventObjects = (Object[]) call.invoke(new Object[]{objects.toArray()});
        return eventObjects;
    }

    public Reaction getReactomeReaction(Long reactomeReactionId) throws ReactomeFetchDataException {
        Call call = reactomeService.getQueryByIdCall();
        Reaction reaction = null;
        try {
            reaction = (Reaction) call.invoke(new Object[]{reactomeReactionId});
        } catch (RemoteException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get the reaction " +reactomeReactionId +" from Reactome WS! ", ex);
        }
        return reaction;
    }

    public Object[] getReactionPathway(String reactomeUrl)throws ReactomeServiceException{
        Long[] pathwayAndReactionIds = null;
        try {
            pathwayAndReactionIds = ReactomeUtil.parseUrl(reactomeUrl);
        } catch (MalformedURLException ex) {
            throw new ReactomeConnectionException(
                    "Failed to create the URL for " +reactomeUrl, ex);
        } catch (IOException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get reaction and pathway ids from Reactome Web site! ", ex);
        }
        Object[] objs = null;
        try {
            objs = getReactionAndPathwayByIds(pathwayAndReactionIds);
        } catch (Exception ex) {
            throw new ReactomeServiceException(ex);
        }
        return objs;
    }

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

    public Summation getSummationById(Long id) throws ReactomeFetchDataException {
        Call call = reactomeService.getQueryByIdCall();
        Summation summation = null;
        try {
            summation = (Summation) call.invoke(new Object[]{id});
        } catch (RemoteException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get the Reaction description " +id +" from Reactome WS! ", ex);
        }
        return summation;
    }

    public void parsePathways(Pathway[] pathways) {
        for (Pathway pathway: pathways) {
            String id = String.valueOf(pathway.getId());
            String name = pathway.getName();
            List<Event> eventList = pathway.getHasComponent();
        }


    }

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



}
