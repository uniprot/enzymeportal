package uk.ac.ebi.ep.centralservice.chembl.service;

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
    "limit",
    "next",
    "offset",
    "previous",
    "total_count"
})
/**
 *
 * @author joseph
 */
public class PageMeta {

    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("next")
    private Object next;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("previous")
    private Object previous;
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The limit
     */
    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    /**
     *
     * @param limit The limit
     */
    @JsonProperty("limit")
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     *
     * @return The next
     */
    @JsonProperty("next")
    public Object getNext() {
        return next;
    }

    /**
     *
     * @param next The next
     */
    @JsonProperty("next")
    public void setNext(Object next) {
        this.next = next;
    }

    /**
     *
     * @return The offset
     */
    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    /**
     *
     * @param offset The offset
     */
    @JsonProperty("offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     *
     * @return The previous
     */
    @JsonProperty("previous")
    public Object getPrevious() {
        return previous;
    }

    /**
     *
     * @param previous The previous
     */
    @JsonProperty("previous")
    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    /**
     *
     * @return The totalCount
     */
    @JsonProperty("total_count")
    public Integer getTotalCount() {
        if (totalCount == null) {
            totalCount = 0;
        }
        return totalCount;
    }

    /**
     *
     * @param totalCount The total_count
     */
    @JsonProperty("total_count")
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
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
