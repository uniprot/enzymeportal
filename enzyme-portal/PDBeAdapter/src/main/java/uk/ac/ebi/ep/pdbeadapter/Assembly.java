/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
"preferred",
"form",
"name",
"assembly_id"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assembly {
   @JsonProperty("preferred")
private Boolean preferred;
@JsonProperty("form")
private String form;
@JsonProperty("name")
private String name;
@JsonProperty("assembly_id")
private String assemblyId;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The preferred
*/
@JsonProperty("preferred")
public Boolean getPreferred() {
return preferred;
}

/**
* 
* @param preferred
* The preferred
*/
@JsonProperty("preferred")
public void setPreferred(Boolean preferred) {
this.preferred = preferred;
}

/**
* 
* @return
* The form
*/
@JsonProperty("form")
public String getForm() {
return form;
}

/**
* 
* @param form
* The form
*/
@JsonProperty("form")
public void setForm(String form) {
this.form = form;
}

/**
* 
* @return
* The name
*/
@JsonProperty("name")
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The assemblyId
*/
@JsonProperty("assembly_id")
public String getAssemblyId() {
return assemblyId;
}

/**
* 
* @param assemblyId
* The assembly_id
*/
@JsonProperty("assembly_id")
public void setAssemblyId(String assemblyId) {
this.assemblyId = assemblyId;
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
        return "Assembly{" + "preferred=" + preferred + ", form=" + form + ", name=" + name + ", assemblyId=" + assemblyId + ", additionalProperties=" + additionalProperties + '}';
    }

     
     
}
