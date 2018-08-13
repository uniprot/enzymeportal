/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.experiment;

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
public class PDBexperiments  {
    
        private final Map<String, List<PDBexperiment>> experiment = new HashMap<>();

    public List<PDBexperiment> get(String name) {
        return experiment.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<PDBexperiment>> any() {
        return experiment;
    }

    @JsonAnySetter
    public void set(String name, List<PDBexperiment> value) {
        experiment.put(name, value);
    }
}
