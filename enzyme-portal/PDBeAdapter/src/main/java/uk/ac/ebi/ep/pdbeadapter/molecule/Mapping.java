/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.molecule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "start",
    "end"
})
public class Mapping {

    @JsonProperty("start")
    private Start start;
    @JsonProperty("end")
    private End end;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The start
     */
    @JsonProperty("start")
    public Start getStart() {
        return start;
    }

    /**
     *
     * @param start The start
     */
    @JsonProperty("start")
    public void setStart(Start start) {
        this.start = start;
    }

    /**
     *
     * @return The end
     */
    @JsonProperty("end")
    public End getEnd() {
        return end;
    }

    /**
     *
     * @param end The end
     */
    @JsonProperty("end")
    public void setEnd(End end) {
        this.end = end;
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
  
        return start+" - "+end;
    }
    
    

}
