/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

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
public class PdbSearchResult {


    private final Map<String, List<PDBe>> pdb = new HashMap<>();

    public List<PDBe> get(String name) {
        return pdb.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<PDBe>> any() {
        return pdb;
    }

    @JsonAnySetter
    public void set(String name, List<PDBe> value) {
        pdb.put(name, value);
    }

}
