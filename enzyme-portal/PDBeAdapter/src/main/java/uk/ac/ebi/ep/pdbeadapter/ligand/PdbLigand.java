package uk.ac.ebi.ep.pdbeadapter.ligand;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
/**
 *
 * @author Joseph
 */
public class PdbLigand {

    private final Map<String, List<Ligand>> legand = new HashMap<>();

    public List<Ligand> get(String name) {
        return legand.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<Ligand>> any() {
        return legand;
    }

    @JsonAnySetter
    public void set(String name, List<Ligand> value) {
        legand.put(name, value);
    }
}
