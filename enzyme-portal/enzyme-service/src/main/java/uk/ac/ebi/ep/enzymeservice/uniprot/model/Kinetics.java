package uk.ac.ebi.ep.enzymeservice.uniprot.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "km",
    "vmax"
})
/**
 *
 * @author joseph
 */
@Data
public class Kinetics {

    @JsonProperty("km")
    private List<Km> km;
    @JsonProperty("vmax")
    private List<Vmax> vmax;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("km")
    public List<Km> getKm() {
        if (km == null) {
            km = new ArrayList<>();
        }
        return km;
    }

    @JsonProperty("km")
    public void setKm(List<Km> km) {
        this.km = km;
    }

    @JsonProperty("vmax")
    public List<Vmax> getVmax() {
        if (vmax == null) {
            vmax = new ArrayList<>();
        }
        return vmax;
    }

    @JsonProperty("vmax")
    public void setVmax(List<Vmax> vmax) {
        this.vmax = vmax;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
