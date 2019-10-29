package uk.ac.ebi.ep.literatureservice.model;

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
 * @author joseph
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fullTextUrl"
})
public class FullTextUrlList {

    @JsonProperty("fullTextUrl")
    private List<FullTextUrl> fullTextUrl = new ArrayList<>();
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The fullTextUrl
     */
    @JsonProperty("fullTextUrl")
    public List<FullTextUrl> getFullTextUrl() {
        return fullTextUrl;
    }

    /**
     *
     * @param fullTextUrl The fullTextUrl
     */
    @JsonProperty("fullTextUrl")
    public void setFullTextUrl(List<FullTextUrl> fullTextUrl) {
        this.fullTextUrl = fullTextUrl;
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
