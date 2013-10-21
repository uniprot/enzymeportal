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
import uk.ac.ebi.rhea.domain.Database;
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

	/**
	 * Retrieves Reactome pathway stable IDs for a given Reactome reaction
	 * stable ID.
	 * @param reactionStableId a Reactome reaction stable ID
	 * @return a list of Reactome pathway stable IDs
	 * @throws BiomartFetchDataException
	 */
	public List<String> getPathwaysByReactionId(String reactionStableId)
	throws BiomartFetchDataException {
		String idWithoutVersion = reactionStableId.split("\\.")[0];
		Object[] reactionIdArg = { idWithoutVersion };
		String getPathwaysByReactionIdQuery = Transformer.getMessageTemplate(
				"getPathwaysByReactionIdQuery", reactionIdArg);
		Object[] getPathwaysByReactionIdQueryArg =
				{ getPathwaysByReactionIdQuery };
		String query = Transformer.getMessageTemplate("queryTpl",
				getPathwaysByReactionIdQueryArg);
		String baseUrl = Transformer.getMessageTemplate("baseUrl");
		URLConnection uCon = sendRequest(baseUrl, query);
		return parsePathwaysResponse(uCon);
	}

	/**
	 * Retrieves reactions from Reactome for a given UniProt accession.
	 * @param accession a UniProt accession
	 * @return a list of ReactionPathway objects with only reactions, whose
	 * 		only xrefs will be plain Reactome pathway stable IDs.
	 * @throws BiomartFetchDataException
	 */
	public List<ReactionPathway> getReactionsByUniprotAccession(String accession)
	throws BiomartFetchDataException {
		Object[] reactionIdArg = { accession };
		String getReactionsByUniprotIdQuery = Transformer.getMessageTemplate(
				"getReactionsByUniprotIdQuery", reactionIdArg);
		Object[] getReactionsByUniprotIdQueryArg =
				{ getReactionsByUniprotIdQuery };
		String query = Transformer.getMessageTemplate("queryTpl",
				getReactionsByUniprotIdQueryArg);
		String baseUrl = Transformer.getMessageTemplate("baseUrl");
		URLConnection uCon = sendRequest(baseUrl, query);
		List<ReactionPathway> results = parseReactionsResponse(uCon);
		return results;

	}

	/**
	 * Retrieves Reactome pathway stable IDs for a given UniProt accession.
	 * @param accession a UniProt accession
	 * @return a list of Reactome pathway stable IDs
	 * @throws BiomartFetchDataException
	 */
	public List<String> getPathwaysByUniprotAccession(String accession)
	throws BiomartFetchDataException {
		Object[] uniprotArg = { accession };
		String getPathwaysByUniprotIdQuery = Transformer.getMessageTemplate(
				"getPathwaysByUniprotIdQuery", uniprotArg);
		Object[] getPathwaysByUniprotIdQueryArg =
				{ getPathwaysByUniprotIdQuery };
		String query = Transformer.getMessageTemplate("queryTpl",
				getPathwaysByUniprotIdQueryArg);
		String baseUrl = Transformer.getMessageTemplate("baseUrl");
		URLConnection uCon = sendRequest(baseUrl, query);
		return parsePathwaysResponse(uCon);
	}

	/**
	 * Prepares the URL and sends a request to BioMart.
	 * 
	 * @param baseUrl the BioMart base URL.
	 * @param query the query (XML) sent to BioMart.
	 * @return a URLConnection.
	 * @throws BiomartFetchDataException
	 *             in case of problem with the URL, its encoding or while
	 *             opening the connection.
	 */
	protected URLConnection sendRequest(String baseUrl, String query)
	throws BiomartFetchDataException {
		URL url = null;
		try {
			url = DataTypeConverter.createEncodedUrl(baseUrl, query);
		} catch (UnsupportedEncodingException ex) {
			throw new BiomartFetchDataException(
					"Failed to encode the url for Biomart request " + baseUrl
					+ " " + query, ex);
		} catch (MalformedURLException ex) {
			throw new BiomartFetchDataException(
					"Failed to create the url for Biomart request " + baseUrl
					+ " " + query, ex);
		}
		URLConnection uCon = null;
		try {
			uCon = url.openConnection();
		} catch (IOException ex) {
			throw new BiomartFetchDataException(
					"Unable to connect to Biomart service to process request "
					+ url.toString(), ex);
		}
		return uCon;
	}

   /**
    * Parses the response from BioMart containing pathways info.
    * @param uCon
    * @return a list of Reactome pathway stable IDs.
    * @throws BiomartFetchDataException in case of error reading from the
    * 		connection, or if BioMart returns an error message.
    */
   private static List<String> parsePathwaysResponse(URLConnection uCon) 
   throws BiomartFetchDataException {
       List<String> results = new ArrayList<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
            String inputLine, errorMsg = "";
            while ((inputLine = in.readLine()) != null) {
            	if (inputLine.indexOf("ERROR") > -1){
            		errorMsg += inputLine;
            	} else {
                    final String pathwayId = inputLine.trim();
                    if (!results.contains(pathwayId)){
    					results.add(pathwayId);
                    }
            	}
            }
            if (errorMsg.length() > 0){
            	LOGGER.error(errorMsg);
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
        LOGGER.debug("Pathways parsed: " + results);
       return results;
   }

	/**
	* Parses the response from BioMart containing reaction info.
	* @param uCon a connection to BioMart.
	* @return a list of ReactionPathway objects containing information only
	* 		about reactions, whose xrefs will be Reactome reaction IDs (not
	* 		whole URLs).
	* @throws BiomartFetchDataException in case of problem reading from the
	* 		connection.
	*/
   private static List<ReactionPathway> parseReactionsResponse(URLConnection uCon)
   throws BiomartFetchDataException {
       List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[]fieldValues = inputLine.split("\t");
                if(fieldValues.length >1){
                ReactionPathway reactionPathway = new ReactionPathway();
                EnzymeReaction enzymeReaction = new EnzymeReaction();
                String reactionId = fieldValues[0].trim();
               
                List<Object> reactomeReactionId = new ArrayList<Object>();
                reactomeReactionId.add(Database.REACTOME.getEntryUrl(reactionId));
                enzymeReaction.setXrefs(reactomeReactionId);
                
                
                String reactionName = fieldValues[1].trim();                
                enzymeReaction.setName(reactionName);
                
                reactionPathway.setReaction(enzymeReaction);
                reactionPathways.add(reactionPathway);
                }
            }
            in.close();
        } catch (IOException ex) {
            throw new BiomartFetchDataException("Failed to read the response of the Url connection" +uCon.toString(), ex);
        }
       return reactionPathways;
   }
}
