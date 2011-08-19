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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis.client.Call;
import org.reactome.cabig.domain.Event;
import org.reactome.cabig.domain.Pathway;
import org.reactome.cabig.domain.Reaction;
import org.reactome.cabig.domain.Summation;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

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

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static void main(String[] args) {
        ServiceInitializer serviceInitializer = new ServiceInitializer();
        //Call diagramCall = serviceInitializer.generatePathwayDiagramInSVGNewCall();
        //Call queryById = serviceInitializer.getQueryByIdCall();
        ReactomeAdapter reactomeAdapter = new ReactomeAdapter();

        try {            
            //testQueryPathwaysForReferenceEntities(getRefCall);
            //generatePathwayDiagramInSVG(diagramCall);
            //reactomeAdapter.parseUrl();
            reactomeAdapter.getReaction("http://www.reactome.org/cgi-bin/eventbrowser_st_id?ST_ID=REACT_6763.1");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public String[] getReaction(String reactomeUrl)throws ReactomeFetchDataException{
        String[] pathwayAndReactionIds = null;
        String[] pathwayidAndReactionDesc = new String[2];
        try {
            pathwayAndReactionIds = this.parseUrl(reactomeUrl);
        } catch (MalformedURLException ex) {
            throw new ReactomeFetchDataException (
                    "Unable create the URL for " +reactomeUrl);
        } catch (IOException ex) {
            Logger.getLogger(ReactomeAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.getPathwaysByXrefIds(null);
        //this.ge
        ServiceInitializer serviceInitializer = new ServiceInitializer();
        Call call = serviceInitializer.getQueryByIdCall();
        StringBuffer sb = new StringBuffer();
        try {
            Reaction reaction = (Reaction)call.invoke(new Object[]{new Long(pathwayAndReactionIds[1])});
            List<Summation> summations = reaction.getSummation();
            for (Summation summation:summations) {
                sb.append(summation.getText());
                sb.append("\n");
            }
            long pathwayId = reaction.getId();
            pathwayidAndReactionDesc[0] =String.valueOf(pathwayId);
            pathwayidAndReactionDesc[1] = sb.toString();
        } catch (RemoteException ex) {
            throw new ReactomeFetchDataException (
                    "Call to Reactome WS failed for reaction " +reactomeUrl);
        }

        return pathwayidAndReactionDesc;
    }

   public String[] parseUrl(String reactomeReactionUrl) throws MalformedURLException, IOException {
        URL url = new URL(reactomeReactionUrl);
        URLConnection uCon = url.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                uCon.getInputStream()));
        String inputLine;
        String pathwayId = null;
        String reactionId = null;
        while ((inputLine = in.readLine()) != null)
        if (inputLine.startsWith("<form")) {
            String formString = inputLine;
            String[] split1 = formString.split("&");
            pathwayId = split1[2].split("=")[1];
            reactionId = split1[3].split("=")[1];
            System.out.println(pathwayId);
            System.out.println(reactionId);
        }
       in.close();
       String[] pathwayAndReactionIds = {pathwayId, reactionId};
       return pathwayAndReactionIds;
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
