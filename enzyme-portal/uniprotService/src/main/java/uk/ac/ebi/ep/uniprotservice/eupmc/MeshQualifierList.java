/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.eupmc;

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
    "meshQualifier"
})
public class MeshQualifierList {

    @JsonProperty("meshQualifier")
    private List<MeshQualifier> meshQualifier = new ArrayList<>();
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The meshQualifier
     */
    @JsonProperty("meshQualifier")
    public List<MeshQualifier> getMeshQualifier() {
        return meshQualifier;
    }

    /**
     *
     * @param meshQualifier The meshQualifier
     */
    @JsonProperty("meshQualifier")
    public void setMeshQualifier(List<MeshQualifier> meshQualifier) {
        this.meshQualifier = meshQualifier;
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
