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
"keyword"
})
public class KeywordList {

@JsonProperty("keyword")
private List<String> keyword = new ArrayList<>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The keyword
*/
@JsonProperty("keyword")
public List<String> getKeyword() {
return keyword;
}

/**
* 
* @param keyword
* The keyword
*/
@JsonProperty("keyword")
public void setKeyword(List<String> keyword) {
this.keyword = keyword;
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
