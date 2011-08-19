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
        Call call = this.reactomeService.getQueryByIdsCall();
        Object[] eventObjects = (Object[]) call.invoke(new Object[]{ids});
        System.out.println("Output from queryByIds(): " + eventObjects.length);
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


    public String[] getReaction(String reactomeUrl)throws ReactomeServiceException{
        Long[] reactionAndPathwayIds = null;
        String[] pathwayidAndReactionDesc = new String[2];
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
        /*
        try {
            Pathway pathway = (Pathway) call.invoke(new Object[]{new Long(pathwayAndReactionIds[0])});
        } catch (RemoteException ex) {
            throw new ReactomeFetchDataException(
                    "Failed to get the pathway from Reactome WS! ", ex);
        }
         *
         */

        long pathwayId = reaction.getId();
        pathwayidAndReactionDesc[0] = String.valueOf(pathwayId);
//        pathwayidAndReactionDesc[1] = ReactomeUtil.getReactionDescription(reaction);

        return pathwayidAndReactionDesc;
    }

    public static void generatePathwayDiagramInSVG(Call call) throws Exception {
        Pathway pathway = new Pathway();
        //pathway.setId(69278L);
        //pathway.setId(15869L);
        pathway.setId(418346);
        String svg = (String) call.invoke(new Object[]{pathway});
        String fileName = "/Users/hongcao/Desktop/Pathways/MitoticCellCycle3.svg";
        //String fileName = "/Users/guanming/Desktop/NucleotideMetabolism.svg";
        FileOutputStream fos = new FileOutputStream(fileName);
        PrintStream ps = new PrintStream(fos);
        ps.print(svg);
        ps.close();
        fos.close();
    }
  

    public static void getPathwaysByXrefIds(Call call, String[] identifiers) throws Exception {
        long time1 = System.currentTimeMillis();
        Reaction[] pathways = (Reaction[]) call.invoke(new Object[]{identifiers});
        long time2 = System.currentTimeMillis();
        System.out.printf("Pathway for a list of identifiers: %d (%d)%n",
                pathways.length, (time2 - time1));
        outputArray(pathways);
    }

    public void parsePathways(Pathway[] pathways) {
        for (Pathway pathway: pathways) {
            String id = String.valueOf(pathway.getId());
            String name = pathway.getName();
            List<Event> eventList = pathway.getHasComponent();
        }


    }
    private static void outputArray(Object[] objects) throws Exception {
        for (Object obj : objects) {
            printOutput(obj);
        }
    }
    private static void printOutput(Object obj) throws Exception {
        Object[] EMPTY_ARG = new Object[]{};
        System.out.printf("%s -> %s%n", obj.getClass(), obj.toString());
        Method[] methods = obj.getClass().getMethods();
        // Get all getMethods
        for (Method m : methods) {
            String mName = m.getName();
            if (mName.startsWith("get")) {
                String propName = lowerFirst(mName.substring(3));
                System.out.printf("\t%s: %s%n", propName, m.invoke(obj, EMPTY_ARG));
            }
        }
    }
    private static String lowerFirst(String propName) {
        return propName.substring(0, 1).toLowerCase() + propName.substring(1);
    }

    public EnzymeModel getPathways(String uniprotAccession) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
