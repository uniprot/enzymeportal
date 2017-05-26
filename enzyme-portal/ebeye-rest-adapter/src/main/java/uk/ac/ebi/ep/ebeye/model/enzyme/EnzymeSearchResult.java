package uk.ac.ebi.ep.ebeye.model.enzyme;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hitCount",
    "entries",
    "facets"
})
public class EnzymeSearchResult {

    @JsonProperty("hitCount")
    private Integer hitCount;
    @JsonProperty("entries")
    private List<EnzymeEntry> entries = new ArrayList<>();
    @JsonProperty("facets")
    private List<Facet> facets = new ArrayList<>();
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The hitCount
     */
    @JsonProperty("hitCount")
    public Integer getHitCount() {
        return hitCount;
    }

    /**
     *
     * @param hitCount The hitCount
     */
    @JsonProperty("hitCount")
    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    /**
     *
     * @return The entries
     */
    @JsonProperty("entries")
    public List<EnzymeEntry> getEntries() {
        return entries;
    }

    /**
     *
     * @param entries The entries
     */
    @JsonProperty("entries")
    public void setEntries(List<EnzymeEntry> entries) {
        this.entries = entries;
    }

    /**
     *
     * @return The facets
     */
    @JsonProperty("facets")
    public List<Facet> getFacets() {
        return facets;
    }

    /**
     *
     * @param facets The facets
     */
    @JsonProperty("facets")
    public void setFacets(List<Facet> facets) {
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

    @Override
    public String toString() {
        return "ModelResult{" + "hitCount=" + hitCount + ", entries=" + entries + ", facets=" + facets + '}';
    }

}
