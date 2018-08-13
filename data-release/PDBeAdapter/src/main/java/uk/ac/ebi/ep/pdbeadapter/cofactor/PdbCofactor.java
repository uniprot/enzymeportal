package uk.ac.ebi.ep.pdbeadapter.cofactor;

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
public class PdbCofactor {

    private final Map<String, List<Cofactor>> cofactors = new HashMap<>();

    public List<Cofactor> get(String name) {
        return cofactors.get(name);
    }

    @JsonAnyGetter
    public Map<String, List<Cofactor>> any() {
        return cofactors;
    }

    @JsonAnySetter
    public void set(String name, List<Cofactor> value) {
        cofactors.put(name, value);
    }
}
