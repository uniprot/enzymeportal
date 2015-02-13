/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.aTest;

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
public class PDBeGlobal<T> {
    
      private final Map<String, List<T>> global = new HashMap<>();

    public List<T> get(String name) {
        return global.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<T>> any() {
        return global;
    }

    @JsonAnySetter
    public void set(String name, List<T> value) {
        global.put(name, value);
    }
}
