
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
"abbreviation",
"qualifierName",
"majorTopic_YN"
})
public class MeshQualifier {

@JsonProperty("abbreviation")
private String abbreviation;
@JsonProperty("qualifierName")
private String qualifierName;
@JsonProperty("majorTopic_YN")
private String majorTopicYN;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The abbreviation
*/
@JsonProperty("abbreviation")
public String getAbbreviation() {
return abbreviation;
}

/**
* 
* @param abbreviation
* The abbreviation
*/
@JsonProperty("abbreviation")
public void setAbbreviation(String abbreviation) {
this.abbreviation = abbreviation;
}

/**
* 
* @return
* The qualifierName
*/
@JsonProperty("qualifierName")
public String getQualifierName() {
return qualifierName;
}

/**
* 
* @param qualifierName
* The qualifierName
*/
@JsonProperty("qualifierName")
public void setQualifierName(String qualifierName) {
this.qualifierName = qualifierName;
}

/**
* 
* @return
* The majorTopicYN
*/
@JsonProperty("majorTopic_YN")
public String getMajorTopicYN() {
return majorTopicYN;
}

/**
* 
* @param majorTopicYN
* The majorTopic_YN
*/
@JsonProperty("majorTopic_YN")
public void setMajorTopicYN(String majorTopicYN) {
this.majorTopicYN = majorTopicYN;
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

