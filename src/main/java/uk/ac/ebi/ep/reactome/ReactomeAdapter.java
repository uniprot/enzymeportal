package uk.ac.ebi.ep.reactome;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.axis.client.Call;
import org.reactome.cabig.domain.Event;
import org.reactome.cabig.domain.Pathway;
import org.reactome.cabig.domain.Reaction;
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


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static void main(String[] args) {
        ServiceInitializer serviceInitializer = new ServiceInitializer();
        Call diagramCall = serviceInitializer.generatePathwayDiagramInSVGNewCall();
        Call getRefCall = serviceInitializer.queryPathwaysForReferenceIdentifiersNewCall();
        try {            
            testQueryPathwaysForReferenceEntities(getRefCall);
            //generatePathwayDiagramInSVG(diagramCall);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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

    public static void testQueryPathwaysForReferenceEntities(Call call) throws Exception {
        long time1 = System.currentTimeMillis();
        //String[] identifiers = new String[3];
        //identifiers[0] = "Q9Y266";
        //identifiers[1] = "P17480";
        //identifiers[2] = "P20248";

        String[] identifiers = new String[1];
        identifiers[0] = "P61218";
        //call = createCall("queryPathwaysForReferenceIdentifiers");
        //Pathway[] pathways = (Pathway[]) call.invoke(new Object[]{identifiers});
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

    public Reaction getReaction(String reactomeReactionAccession) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
