/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"dbName"
})
/**
 *
 * @author joseph
 */
public class DbCrossReferenceList {

@JsonProperty("dbName")
private List<String> dbName = new ArrayList<>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The dbName
*/
@JsonProperty("dbName")
public List<String> getDbName() {
return dbName;
}

/**
* 
* @param dbName
* The dbName
*/
@JsonProperty("dbName")
public void setDbName(List<String> dbName) {
this.dbName = dbName;
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