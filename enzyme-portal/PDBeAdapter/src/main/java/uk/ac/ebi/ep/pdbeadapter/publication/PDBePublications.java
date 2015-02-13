/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.publication;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PDBePublications {
    
     private final Map<String, List<PDBePublication>> publication = new HashMap<>();

    public List<PDBePublication> get(String name) {
        return publication.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<PDBePublication>> any() {
        return publication;
    }

    @JsonAnySetter
    public void set(String name, List<PDBePublication> value) {
        publication.put(name, value);
    }
}
