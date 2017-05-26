package uk.ac.ebi.ep.ebeye.model.proteinGroup;

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

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hitCount",
    "entries",
    "facets"
})
public class ProteinGroupSearchResult {

    @JsonProperty("hitCount")
    private Integer hitCount;
    @JsonProperty("entries")
    private List<ProteinGroupEntry> entries = new ArrayList<>();
    @JsonProperty("facets")
    private List<Object> facets = new ArrayList<>();
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("hitCount")
    public Integer getHitCount() {
        return hitCount;
    }

    @JsonProperty("hitCount")
    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    @JsonProperty("entries")
    public List<ProteinGroupEntry> getEntries() {
        return entries;
    }

    @JsonProperty("entries")
    public void setEntries(List<ProteinGroupEntry> entries) {
        this.entries = entries;
    }

    @JsonProperty("facets")
    public List<Object> getFacets() {
        return facets;
    }

    @JsonProperty("facets")
    public void setFacets(List<Object> facets) {
        this.facets = facets;
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
