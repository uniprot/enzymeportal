package uk.ac.ebi.ep.enzymeservices.reactome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.reactome.cabig.domain.Event;
import org.reactome.cabig.domain.Summation;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeUtil {

   public static Long[] parseUrl(String reactomeReactionUrl) throws MalformedURLException, IOException {
	   Long[] pathwayAndReactionIds = null;
	   InputStreamReader isr = null;
       BufferedReader in = null;
		try {
		    URL url = new URL(reactomeReactionUrl);
		    URLConnection uCon = url.openConnection();
		    isr = new InputStreamReader(uCon.getInputStream());
			in = new BufferedReader(isr);
		    String inputLine;
		    while ((inputLine = in.readLine()) != null)
		    if (inputLine.startsWith("<form")) {
				String formString = inputLine;
				String[] split1 = formString.split("&");
				String pathwayId = split1[2].split("=")[1];
				String reactionId = split1[3].split("=")[1];
			    pathwayAndReactionIds = new Long[]{
			    		new Long(reactionId), new Long(pathwayId)
	    		};
		    }
		} finally {
		    if (in != null) in.close();
		    if (isr != null) isr.close();
		}
		return pathwayAndReactionIds;
   }

    public static String getReactionDescription(Object eventObj) {
        StringBuffer sb = new StringBuffer();
        Event event = (Event)eventObj;
        List<Summation> summations = event.getSummation();
        for (Summation summation:summations) {
            String text = summation.getText();
            if (text != null) {
                sb.append(summation.getText());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

   public static String parseReactomeHtml(String stableId, String parseTerm) throws MalformedURLException, IOException {
       String reactionDesc = null;
	   URL oracle = new URL(Constant.REACTOME_SEARCH_URL +stableId);
        URLConnection uCon = oracle.openConnection();
        InputStreamReader isr = null;
		BufferedReader in = null;
		try {
	        isr = new InputStreamReader(uCon.getInputStream());
			in = new BufferedReader(isr);
	        String inputLine;
	        boolean fetch = false;
	        while ((inputLine = in.readLine()) != null) {
	            if(fetch == true) {
	                reactionDesc = inputLine;
	                break;
	            }
	            if (inputLine.contains(parseTerm)) {
	                fetch = true;
	                in.readLine();
	            }
	        }
		} finally {
			if (isr != null) isr.close();
			if (in != null) in.close();
		}
       return reactionDesc;
   }

}
