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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"subset"
})


/**
 *
 * @author joseph
 */
public class SubsetList {
    @JsonProperty("subset")
private List<Subset> subset = new ArrayList<>();
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The subset
*/
@JsonProperty("subset")
public List<Subset> getSubset() {
return subset;
}

/**
* 
* @param subset
* The subset
*/
@JsonProperty("subset")
public void setSubset(List<Subset> subset) {
this.subset = subset;
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
