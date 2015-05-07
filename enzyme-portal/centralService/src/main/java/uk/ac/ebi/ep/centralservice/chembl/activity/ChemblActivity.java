/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.activity;

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
import uk.ac.ebi.ep.centralservice.chembl.service.PageMeta;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"activities",
"page_meta"
})
/**
 *
 * @author joseph
 */
public class ChemblActivity {
    @JsonProperty("activities")
private List<Activity> activities = new ArrayList<>();
@JsonProperty("page_meta")
private PageMeta pageMeta;
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The activities
*/
@JsonProperty("activities")
public List<Activity> getActivities() {
return activities;
}

/**
* 
* @param activities
* The activities
*/
@JsonProperty("activities")
public void setActivities(List<Activity> activities) {
this.activities = activities;
}

/**
* 
* @return
* The pageMeta
*/
@JsonProperty("page_meta")
public PageMeta getPageMeta() {
return pageMeta;
}

/**
* 
* @param pageMeta
* The page_meta
*/
@JsonProperty("page_meta")
public void setPageMeta(PageMeta pageMeta) {
this.pageMeta = pageMeta;
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
