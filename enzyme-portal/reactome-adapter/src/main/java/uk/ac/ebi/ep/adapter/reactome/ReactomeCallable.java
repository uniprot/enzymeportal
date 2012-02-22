package uk.ac.ebi.ep.adapter.reactome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.rhea.domain.Database;

/**
 * Class to retrieve pathways from Reactome HTML pages.
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeCallable {

	private static final Logger LOGGER = Logger.getLogger(ReactomeCallable.class);
	
    public static class GetPathwayCaller implements Callable<Pathway> {
        protected String pathwayId;
        protected ReactomeConfig config;

        public GetPathwayCaller(String pathwayId, ReactomeConfig config) {
            this.pathwayId = pathwayId;
            this.config = config;
        }

        
        public Pathway call() throws Exception {
            return parseReactomeEntryPage(this.pathwayId);
        }
        
//        public ReactionPathway buildprovenance(){
//            ReactionPathway reactionPathway = new ReactionPathway();
//           // reactionPathway.setPathways();
//            String a = "Data source";
//            String b = " Released Date";
//            List<String> list = new LinkedList<String>();
//            list.add(a);
//            list.add(b);
//            reactionPathway.setProvenance(list);
//            
//             return reactionPathway;
//        }

        public Pathway parseReactomeEntryPage(String stableId)
		throws MalformedURLException, IOException {
            Pathway pathway = new Pathway();
            String charset = "UTF-8";
            String query = String.format("ST_ID=%s",
            URLEncoder.encode(stableId, charset));
            URLConnection uCon = null;
            BufferedReader in = null;
            try {
            	LOGGER.debug(" -RP- before openConnection");
                final URL url = new URL(Constant.REACTOME_ENTRY_URL + "?" + query);
				uCon = config.getUseProxy()?
						url.openConnection() : url.openConnection(Proxy.NO_PROXY);
                uCon.addRequestProperty("Cookie", "ClassicView=1");
                uCon.setReadTimeout(config.getTimeout());
            	LOGGER.debug(" -RP- before getInputStream");
                in = new BufferedReader(
                        new InputStreamReader(
                        uCon.getInputStream()));
                String inputLine;
                String description = null;
                String pathwayName = null;
                String image = null;
            	LOGGER.debug(" -RP- before processing HTML");
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
            	LOGGER.debug(" -RP- before building pathway");
	            pathway.setId(stableId);
	            pathway.setName(pathwayName);
	            pathway.setDescription(description);
	            pathway.setUrl(Database.REACTOME.getEntryUrl(stableId));
	            pathway.setImage(image);
            } finally {
            	if (in != null) in.close();
            }
        	LOGGER.debug(" -RP- before returning");
            return pathway;
        }
    }
}
