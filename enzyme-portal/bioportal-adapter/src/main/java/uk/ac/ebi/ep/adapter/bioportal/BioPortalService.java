/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.adapter.bioportal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.enzyme.model.Disease;

/**
 *
 * @author joseph
 */
public class BioPortalService implements IBioportalAdapter {

    static final String REST_URL = "http://data.bioontology.org";
    String API_KEY = "9f19fdf6-82d0-4335-97a1-f71d3ce156f6";
    static final ObjectMapper mapper = new ObjectMapper();
    static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    static final String DEFINITION = "definition";
    static final String LABEL = "prefLabel";

    private static final Logger LOGGER = Logger.getLogger(BioPortalService.class);
    //http://data.bioontology.org/search?q=D017827&apikey=9f19fdf6-82d0-4335-97a1-f71d3ce156f6&ontology=EFO&exact_match=true

//http://data.bioontology.org/ontologies/EFO
    @Override
    public Disease getDisease(String term) throws BioportalAdapterException {
        String definition = "";

        // Reader reader = new StringReader(get(REST_URL + "/search?q=" + term+"&ontology={EFO,MeSH,OMIM}&exact_match=true"));
        Reader reader = new StringReader(get(REST_URL + "/search?q=" + term));

        JsonReader jsonReader = Json.createReader(reader);

        JsonObject jo = jsonReader.readObject();

        JsonArray jsonArray = jo.getJsonArray("collection");
        Disease disease = null;
        for (JsonObject obj : jsonArray.getValuesAs(JsonObject.class)) {
            disease = new Disease();
            disease.setId(term);
            for (Map.Entry<String, JsonValue> entry : obj.entrySet()) {

                if (entry.getKey().equalsIgnoreCase(LABEL)) {
                    String name = entry.getValue().toString();
                           disease.setName(name);

                }
                if (entry.getKey().equalsIgnoreCase(DEFINITION)) {

                    definition = entry.getValue().toString().replace("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
                    disease.setDescription(definition);
                }

            }
        }
       
        return disease;
    }

    private JsonNode jsonToNode(String json) {
        JsonNode root = null;

        try {

            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {

            LOGGER.fatal("JsonProcessingException while Processing the Json result", e);
        } catch (IOException e) {
            LOGGER.fatal("IOException while Reading from the Bioportal service", e);
        }
        return root;
    }

    private String get(String urlToGet) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";

        //see enzyme portal data for a better implementation using env to get api key. this impl is temporal
        String bioportal_api_key = API_KEY;

        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + bioportal_api_key);
            conn.setRequestProperty("Accept", "application/json");
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;

            }
            rd.close();
        } catch (IOException e) {

            //log
            LOGGER.fatal("ERROR Connecting to BioPortal Service", e);
        }
        return result;
    }

    @Override
    public Disease getDiseaseByName(String term, String diseaseId) throws BioportalAdapterException {
        String definition = "";

        // Reader reader = new StringReader(get(REST_URL + "/search?q=" + term+"&ontology={EFO,MeSH,OMIM}&exact_match=true"));
        Reader reader = new StringReader(get(REST_URL + "/search?q=" + term));

        JsonReader jsonReader = Json.createReader(reader);

        JsonObject jo = jsonReader.readObject();

        JsonArray jsonArray = jo.getJsonArray("collection");
        Disease disease = null;
        for (JsonObject obj : jsonArray.getValuesAs(JsonObject.class)) {
            disease = new Disease();
            disease.setId(diseaseId);
            for (Map.Entry<String, JsonValue> entry : obj.entrySet()) {

                switch (entry.getKey()) {
                    case LABEL:
                        if (entry.getKey().equalsIgnoreCase(LABEL)) {
                            String name = entry.getValue().toString();
                            name = name.replaceAll("^\"|\"$", "");
                            if (name.equalsIgnoreCase(term)) {
                                disease.setName(name);

                            }

                        }
                        break;
                    case DEFINITION:
                        if (entry.getKey().equalsIgnoreCase(DEFINITION)) {
                            definition = entry.getValue().toString().replace("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
                           
                            disease.setDescription(definition);
                            return disease;
                        }
                        break;

                }


            }
        }
  
        return disease;
    }

    @Override
    public void setConfig(BioportalConfig config) {
        //config not used here
    }

}
