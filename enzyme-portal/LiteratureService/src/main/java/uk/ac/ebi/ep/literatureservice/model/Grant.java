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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joseph
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"grantId",
"agency",
"acronym",
"orderIn"
})
public class Grant {

@JsonProperty("grantId")
private String grantId;
@JsonProperty("agency")
private String agency;
@JsonProperty("acronym")
private String acronym;
@JsonProperty("orderIn")
private Integer orderIn;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The grantId
*/
@JsonProperty("grantId")
public String getGrantId() {
return grantId;
}

/**
* 
* @param grantId
* The grantId
*/
@JsonProperty("grantId")
public void setGrantId(String grantId) {
this.grantId = grantId;
}

/**
* 
* @return
* The agency
*/
@JsonProperty("agency")
public String getAgency() {
return agency;
}

/**
* 
* @param agency
* The agency
*/
@JsonProperty("agency")
public void setAgency(String agency) {
this.agency = agency;
}

/**
* 
* @return
* The acronym
*/
@JsonProperty("acronym")
public String getAcronym() {
return acronym;
}

/**
* 
* @param acronym
* The acronym
*/
@JsonProperty("acronym")
public void setAcronym(String acronym) {
this.acronym = acronym;
}

/**
* 
* @return
* The orderIn
*/
@JsonProperty("orderIn")
public Integer getOrderIn() {
return orderIn;
}

/**
* 
* @param orderIn
* The orderIn
*/
@JsonProperty("orderIn")
public void setOrderIn(Integer orderIn) {
this.orderIn = orderIn;
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
