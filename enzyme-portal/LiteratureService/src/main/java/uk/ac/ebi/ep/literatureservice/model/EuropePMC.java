package uk.ac.ebi.ep.literatureservice.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "version",
    "hitCount",
    "request",
    "resultList"
})
/**
 *
 * @author joseph
 */
public class EuropePMC {

    @JsonProperty("version")
    private String version;
    @JsonProperty("hitCount")
    private Integer hitCount;
    @JsonProperty("request")
    private Request request;
    @JsonProperty("resultList")
    private ResultList resultList;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The version
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version The version
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

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
     * @return The request
     */
    @JsonProperty("request")
    public Request getRequest() {
        return request;
    }

    /**
     *
     * @param request The request
     */
    @JsonProperty("request")
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     *
     * @return The resultList
     */
    @JsonProperty("resultList")
    public ResultList getResultList() {
        if(resultList == null){
            resultList = new ResultList();
        }
        return resultList;
    }

    /**
     *
     * @param resultList The resultList
     */
    @JsonProperty("resultList")
    public void setResultList(ResultList resultList) {
        this.resultList = resultList;
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
