/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.molecule;

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
public class PDBmolecules {
    
      private final Map<String, List<Molecule>> molecule = new HashMap<>();

    public List<Molecule> get(String name) {
        return molecule.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<Molecule>> any() {
        return molecule;
    }

    @JsonAnySetter
    public void set(String name, List<Molecule> value) {
        molecule.put(name, value);
    }
}
