package uk.ac.ebi.ep.ebeye.search;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeSearchResult {

    @JsonProperty("hitCount")
    private Integer hitCount;
    @JsonProperty("entries")
    private List<Entry> entries;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * @return The hitCount
     */
    @JsonProperty("hitCount")
    public Integer getHitCount() {
        return hitCount;
    }

    /**
     * @param hitCount The hitCount
     */
    @JsonProperty("hitCount")
    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    /**
     * @return The entries
     */
    @JsonProperty("entries")
    public List<Entry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }

        return entries;
    }

    /**
     * @param entries The entries
     */
    @JsonProperty("entries")
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
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
