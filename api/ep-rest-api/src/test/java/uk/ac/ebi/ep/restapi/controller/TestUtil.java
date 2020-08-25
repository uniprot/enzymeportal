package uk.ac.ebi.ep.restapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author joseph
 */
public final class TestUtil {

    private TestUtil() {
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public static String getValueFromJsonData(String jsonData, String nodeName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(jsonData);
        return nodes.findValue(nodeName).textValue();

    }

    public static List<String> getValuesFromJsonData(String jsonData, String nodeName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(jsonData);
        return nodes.findValuesAsText(nodeName);

    }

    public static List<JsonNode> getJsonNodeFromJsonData(String jsonData, String nodeName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(jsonData);
        return nodes.findValues(nodeName);

    }

}
