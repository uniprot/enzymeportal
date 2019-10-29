
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
"majorTopic_YN",
"descriptorName",
"meshQualifierList"
})
public class MeshHeading {

@JsonProperty("majorTopic_YN")
private String majorTopicYN;
@JsonProperty("descriptorName")
private String descriptorName;
@JsonProperty("meshQualifierList")
private MeshQualifierList meshQualifierList;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

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

/**
* 
* @return
* The descriptorName
*/
@JsonProperty("descriptorName")
public String getDescriptorName() {
return descriptorName;
}

/**
* 
* @param descriptorName
* The descriptorName
*/
@JsonProperty("descriptorName")
public void setDescriptorName(String descriptorName) {
this.descriptorName = descriptorName;
}

/**
* 
* @return
* The meshQualifierList
*/
@JsonProperty("meshQualifierList")
public MeshQualifierList getMeshQualifierList() {
return meshQualifierList;
}

/**
* 
* @param meshQualifierList
* The meshQualifierList
*/
@JsonProperty("meshQualifierList")
public void setMeshQualifierList(MeshQualifierList meshQualifierList) {
this.meshQualifierList = meshQualifierList;
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

