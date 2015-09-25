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
"availability",
"availabilityCode",
"documentStyle",
"site",
"url"
})
public class FullTextUrl {

@JsonProperty("availability")
private String availability;
@JsonProperty("availabilityCode")
private String availabilityCode;
@JsonProperty("documentStyle")
private String documentStyle;
@JsonProperty("site")
private String site;
@JsonProperty("url")
private String url;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The availability
*/
@JsonProperty("availability")
public String getAvailability() {
return availability;
}

/**
* 
* @param availability
* The availability
*/
@JsonProperty("availability")
public void setAvailability(String availability) {
this.availability = availability;
}

/**
* 
* @return
* The availabilityCode
*/
@JsonProperty("availabilityCode")
public String getAvailabilityCode() {
return availabilityCode;
}

/**
* 
* @param availabilityCode
* The availabilityCode
*/
@JsonProperty("availabilityCode")
public void setAvailabilityCode(String availabilityCode) {
this.availabilityCode = availabilityCode;
}

/**
* 
* @return
* The documentStyle
*/
@JsonProperty("documentStyle")
public String getDocumentStyle() {
return documentStyle;
}

/**
* 
* @param documentStyle
* The documentStyle
*/
@JsonProperty("documentStyle")
public void setDocumentStyle(String documentStyle) {
this.documentStyle = documentStyle;
}

/**
* 
* @return
* The site
*/
@JsonProperty("site")
public String getSite() {
return site;
}

/**
* 
* @param site
* The site
*/
@JsonProperty("site")
public void setSite(String site) {
this.site = site;
}

/**
* 
* @return
* The url
*/
@JsonProperty("url")
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
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
