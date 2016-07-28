/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "label",
    "value",
    "count"
})
public class FacetValue {

    @JsonProperty("label")
    private String label;
    @JsonProperty("value")
    private String value;
    @JsonProperty("count")
    private Integer count;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The label
     */
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param label The label
     */
    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return The value
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value The value
     */
    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return The count
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count The count
     */
    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
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
        return "FacetValue{" + "label=" + label + ", value=" + value + ", count=" + count + '}';
    }

}
