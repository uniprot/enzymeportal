package uk.ac.ebi.ep.reactome;

import java.util.concurrent.Callable;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import uk.ac.ebi.rhea.domain.Database;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeCallable {

//********************************* VARIABLES ********************************//
//******************************** INNER CLASS *******************************//
    public static class GetPathwayCaller implements Callable<Pathway> {
        protected String pathwayId;

        public GetPathwayCaller(String pathwayId) {
            this.pathwayId = pathwayId;
        }

        
        public Pathway call() throws Exception {
            return parseReactomeEntryPage(this.pathwayId);
        }

        public Pathway parseReactomeEntryPage(
                String stableId) throws MalformedURLException, IOException {
            Pathway pathway = new Pathway();
            String charset = "UTF-8";
            String query = String.format("ST_ID=%s",
            URLEncoder.encode(stableId, charset));
            URLConnection uCon = new URL(Constant.REACTOME_ENTRY_URL + "?" + query).openConnection();
            uCon.addRequestProperty("Cookie", "ClassicView=1");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    uCon.getInputStream()));
            String inputLine;
            String description = null;
            String pathwayName = null;
            String image = null;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("\"eventname\"")) {
                    //<TR><TD CLASS="eventname" COLSPAN="2">CDT1 association with the CDC6:ORC:origin complex</TD></TR>
                    pathwayName =
                            new String(inputLine.replaceAll(
                            "<TR><TD CLASS=\"eventname\" COLSPAN=\"2\">|</TD></TR>", "").trim());
                    System.out.println(pathwayName);
                }
                if (inputLine.contains("\"summation\"")) {
                    //Advance 1 line
                    inputLine = in.readLine();
                    description =
                            new String(inputLine.replaceAll(
                            "<TR><TD CLASS=\"summation\" COLSPAN=\"2\">|</TD></TR>", "").trim());
                    System.out.println(description);
                }
                if (inputLine.contains("\"figure\"")) {
                    image =
                            new String(inputLine.replaceAll(
                            "<TR><TD CLASS=\"figure\" COLSPAN=\"2\"><IMG SRC=\"|\" /></TD></TR>", "").trim());
                    System.out.println(image);
                    break;
                }
            }
            in.close();
            pathway.setId(stableId);
            pathway.setName(pathwayName);
            pathway.setDescription(description);
            pathway.setUrl(Database.REACTOME.getEntryUrl(stableId));
            pathway.setImage(image);
            return pathway;
        }
    }
}
