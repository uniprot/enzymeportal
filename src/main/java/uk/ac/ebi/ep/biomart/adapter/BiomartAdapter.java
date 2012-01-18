package uk.ac.ebi.ep.biomart.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class BiomartAdapter {

	private static final Logger LOGGER = Logger.getLogger(BiomartAdapter.class);

  public static List<String> limitResults(List<String> results) {
    List<String> subResults= null;
    if (results.size() > 0) {
        subResults= results.subList(0, 2);
    } else {
        subResults = results;
    }
    return subResults;
  }
  public List<String> getPathwaysByReactionId(String reactionStableId) throws BiomartFetchDataException{
    //ResourceBundle messages = ResourceBundle.getBundle("MessageBundle",new Locale("en","UK"));
    String idWithoutVersion = reactionStableId.split("\\.")[0];
    Object[] reactionIdArg = {idWithoutVersion};
    String getPathwaysByReactionIdQuery = Transformer.getMessageTemplate("getPathwaysByReactionIdQuery", reactionIdArg);
    Object[] getPathwaysByReactionIdQueryArg = {getPathwaysByReactionIdQuery};
    String query = Transformer.getMessageTemplate("queryTpl", getPathwaysByReactionIdQueryArg);
    String baseUrl = Transformer.getMessageTemplate("baseUrl");
    //System.out.println(request);
    URLConnection uCon = sendRequest(baseUrl,query);
    List<String> results = parsePathwaysResponse(uCon);
    //Test Reactome is too slow to handle more than 2
    //return results;
    return limitResults(results);
  }

  public List<ReactionPathway> getReactionsByUniprotAccession(String accession) throws BiomartFetchDataException{
    Object[] reactionIdArg = {accession};
    String getReactionsByUniprotIdQuery = Transformer.getMessageTemplate("getReactionsByUniprotIdQuery", reactionIdArg);
    Object[] getReactionsByUniprotIdQueryArg = {getReactionsByUniprotIdQuery};
    String query = Transformer.getMessageTemplate("queryTpl", getReactionsByUniprotIdQueryArg);
    String baseUrl = Transformer.getMessageTemplate("baseUrl");
    //List<String> resutls = sendRequest(baseUrl,query);
    URLConnection uCon = sendRequest(baseUrl,query);
    List<ReactionPathway> results = parseReactionsResponse(uCon);
    return results;

  }

  public List<String> getPathwaysByUniprotAccession(String accession) throws BiomartFetchDataException{
    Object[] uniprotArg = {accession};
    String getPathwaysByUniprotIdQuery = Transformer.getMessageTemplate("getPathwaysByUniprotIdQuery", uniprotArg);
    Object[] getPathwaysByUniprotIdQueryArg = {getPathwaysByUniprotIdQuery};
    String query = Transformer.getMessageTemplate("queryTpl", getPathwaysByUniprotIdQueryArg);
    String baseUrl = Transformer.getMessageTemplate("baseUrl");
    //List<String> resutls = sendRequest(baseUrl,query);
    URLConnection uCon = sendRequest(baseUrl,query);
    List<String> results = parsePathwaysResponse(uCon);
    //return resutls;
    return limitResults(results);
  }


   public URLConnection sendRequest(String baseUrl, String query) throws BiomartFetchDataException {
    URL url = null;
        try {
            url = DataTypeConverter.createEncodedUrl(baseUrl, query);
            //System.out.println(request);
        } catch (UnsupportedEncodingException ex) {
             throw new BiomartFetchDataException("Failed to encode the url for Biomart request "
            		 + baseUrl + " " + query, ex);
        } catch (MalformedURLException ex) {
            throw new BiomartFetchDataException("Failed to create the url for Biomart request "
           		 + baseUrl + " " + query, ex);
        }

        URLConnection uCon = null;
        try {
            uCon = url.openConnection();
        } catch (IOException ex) {
            throw new BiomartFetchDataException("Unable to connect to Biomart service to process request " +url.toString(), ex);
        }

       return uCon;
   }

   public static List<String> parsePathwaysResponse(URLConnection uCon) throws BiomartFetchDataException {
       List<String> results = new ArrayList<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
            String inputLine, errorMsg = "";
            while ((inputLine = in.readLine()) != null) {
            	if (inputLine.indexOf("ERROR") > -1){
            		errorMsg += inputLine;
            	} else {
                    results.add(inputLine.trim());
            	}
            }
            if (errorMsg.length() > 0){
        		throw new BiomartFetchDataException(errorMsg);
        	}

        } catch (IOException ex) {
            throw new BiomartFetchDataException("Failed to read the response of the Url connection"
            		+ uCon.toString(), ex);
        } finally {
            try {
				in.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close BufferedReader", e);
			}
        }
        System.out.println(results);
       return results;
   }

   public static List<ReactionPathway> parseReactionsResponse(URLConnection uCon) throws BiomartFetchDataException {
       List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[]fieldValues = inputLine.split("\t");
                ReactionPathway reactionPathway = new ReactionPathway();
                EnzymeReaction enzymeReaction = new EnzymeReaction();
                String reactionId = fieldValues[0].trim();
                List<Object> reactomeReactionId = new ArrayList<Object>();
                reactomeReactionId.add(reactionId);
                enzymeReaction.setXrefs(reactomeReactionId);
                String reactionName = fieldValues[1].trim();                
                enzymeReaction.setName(reactionName);
                reactionPathway.setReaction(enzymeReaction);
                reactionPathways.add(reactionPathway);
            }
            in.close();
        } catch (IOException ex) {
            throw new BiomartFetchDataException("Failed to read the response of the Url connection" +uCon.toString(), ex);
        }
       return reactionPathways;
   }
}