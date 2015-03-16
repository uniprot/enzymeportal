/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class BioPortalService {

    static final String REST_URL = "http://data.bioontology.org";
    static final String API_KEY = "9f19fdf6-82d0-4335-97a1-f71d3ce156f6";
    static final ObjectMapper mapper = new ObjectMapper();
    static final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    static final String DEFINITION = "definition";

    @Autowired
    private Environment env;

    private static final Logger LOGGER = Logger.getLogger(BioPortalService.class);
    
//http://data.bioontology.org/ontologies/EFO
    @Transactional
    public String getDiseaseDescription(String term) {
        String definition = "";

       // Reader reader = new StringReader(get(REST_URL + "/search?q=" + term+"&ontology={EFO,MeSH,OMIM}&exact_match=true"));
        Reader reader = new StringReader(get(REST_URL + "/search?q=" + term));

        JsonReader jsonReader = Json.createReader(reader);

        JsonObject jo = jsonReader.readObject();

        JsonArray jsonArray = jo.getJsonArray("collection");
        for (JsonObject obj : jsonArray.getValuesAs(JsonObject.class)) {

            for (Map.Entry<String, JsonValue> entry : obj.entrySet()) {

                if (entry.getKey().equalsIgnoreCase(DEFINITION)) {

                    definition = entry.getValue().toString().replace("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
                }

            }
        }

        return definition;
    }

    public JsonNode jsonToNode(String json) {
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
        String bioportalApiKey = env.getRequiredProperty("bioportal.api.key");
        if (bioportalApiKey == null) {
            bioportalApiKey = API_KEY;
            LOGGER.error("BioPortal Key could not be retrieved from the environment. default key is now being used");
        }

        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + bioportalApiKey);
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

}
