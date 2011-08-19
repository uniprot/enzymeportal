package uk.ac.ebi.ep.reactome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.reactome.cabig.domain.Reaction;
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

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

   public static Long[] parseUrl(String reactomeReactionUrl) throws MalformedURLException, IOException {
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
       Long[] pathwayAndReactionIds = {new Long(reactionId), new Long(pathwayId)};
       return pathwayAndReactionIds;
   }

    public static String getReactionDescription(Reaction reaction) {
        StringBuffer sb = new StringBuffer();
        List<Summation> summations = reaction.getSummation();
        for (Summation summation:summations) {
            sb.append(summation.getText());
            sb.append("\n");
        }
        return sb.toString();
    }
}
