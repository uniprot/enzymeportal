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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joseph
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"fullName",
"firstName",
"lastName",
"initials",
"affiliation"
})
public class Author {

@JsonProperty("fullName")
private String fullName;
@JsonProperty("firstName")
private String firstName;
@JsonProperty("lastName")
private String lastName;
@JsonProperty("initials")
private String initials;
@JsonProperty("affiliation")
private String affiliation;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The fullName
*/
@JsonProperty("fullName")
public String getFullName() {
return fullName;
}

/**
* 
* @param fullName
* The fullName
*/
@JsonProperty("fullName")
public void setFullName(String fullName) {
this.fullName = fullName;
}

/**
* 
* @return
* The firstName
*/
@JsonProperty("firstName")
public String getFirstName() {
return firstName;
}

/**
* 
* @param firstName
* The firstName
*/
@JsonProperty("firstName")
public void setFirstName(String firstName) {
this.firstName = firstName;
}

/**
* 
* @return
* The lastName
*/
@JsonProperty("lastName")
public String getLastName() {
return lastName;
}

/**
* 
* @param lastName
* The lastName
*/
@JsonProperty("lastName")
public void setLastName(String lastName) {
this.lastName = lastName;
}

/**
* 
* @return
* The initials
*/
@JsonProperty("initials")
public String getInitials() {
return initials;
}

/**
* 
* @param initials
* The initials
*/
@JsonProperty("initials")
public void setInitials(String initials) {
this.initials = initials;
}


/**
* 
* @return
* The affiliation
*/
@JsonProperty("affiliation")
public String getAffiliation() {
return affiliation;
}

/**
* 
* @param affiliation
* The affiliation
*/
@JsonProperty("affiliation")
public void setAffiliation(String affiliation) {
this.affiliation = affiliation;
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
