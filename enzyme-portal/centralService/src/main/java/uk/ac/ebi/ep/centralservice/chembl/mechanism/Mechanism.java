/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.mechanism;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"action_type",
"binding_site_comment",
"direct_interaction",
"disease_efficacy",
"mec_id",
"mechanism_comment",
"mechanism_of_action",
"molecular_mechanism",
"molecule_chembl_id",
"record_id",
"selectivity_comment",
"site_id",
"target_chembl_id"
})
/**
 *
 * @author joseph
 */
public class Mechanism {

@JsonProperty("action_type")
private String actionType;
@JsonProperty("binding_site_comment")
private Object bindingSiteComment;
@JsonProperty("direct_interaction")
private Boolean directInteraction;
@JsonProperty("disease_efficacy")
private Boolean diseaseEfficacy;
@JsonProperty("mec_id")
private Integer mecId;
@JsonProperty("mechanism_comment")
private Object mechanismComment;
@JsonProperty("mechanism_of_action")
private String mechanismOfAction;
@JsonProperty("molecular_mechanism")
private Boolean molecularMechanism;
@JsonProperty("molecule_chembl_id")
private String moleculeChemblId;
@JsonProperty("record_id")
private Integer recordId;
@JsonProperty("selectivity_comment")
private Object selectivityComment;
@JsonProperty("site_id")
private Object siteId;
@JsonProperty("target_chembl_id")
private String targetChemblId;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The actionType
*/
@JsonProperty("action_type")
public String getActionType() {
return actionType;
}

/**
* 
* @param actionType
* The action_type
*/
@JsonProperty("action_type")
public void setActionType(String actionType) {
this.actionType = actionType;
}

/**
* 
* @return
* The bindingSiteComment
*/
@JsonProperty("binding_site_comment")
public Object getBindingSiteComment() {
return bindingSiteComment;
}

/**
* 
* @param bindingSiteComment
* The binding_site_comment
*/
@JsonProperty("binding_site_comment")
public void setBindingSiteComment(Object bindingSiteComment) {
this.bindingSiteComment = bindingSiteComment;
}

/**
* 
* @return
* The directInteraction
*/
@JsonProperty("direct_interaction")
public Boolean getDirectInteraction() {
return directInteraction;
}

/**
* 
* @param directInteraction
* The direct_interaction
*/
@JsonProperty("direct_interaction")
public void setDirectInteraction(Boolean directInteraction) {
this.directInteraction = directInteraction;
}

/**
* 
* @return
* The diseaseEfficacy
*/
@JsonProperty("disease_efficacy")
public Boolean getDiseaseEfficacy() {
return diseaseEfficacy;
}

/**
* 
* @param diseaseEfficacy
* The disease_efficacy
*/
@JsonProperty("disease_efficacy")
public void setDiseaseEfficacy(Boolean diseaseEfficacy) {
this.diseaseEfficacy = diseaseEfficacy;
}

/**
* 
* @return
* The mecId
*/
@JsonProperty("mec_id")
public Integer getMecId() {
return mecId;
}

/**
* 
* @param mecId
* The mec_id
*/
@JsonProperty("mec_id")
public void setMecId(Integer mecId) {
this.mecId = mecId;
}

/**
* 
* @return
* The mechanismComment
*/
@JsonProperty("mechanism_comment")
public Object getMechanismComment() {
return mechanismComment;
}

/**
* 
* @param mechanismComment
* The mechanism_comment
*/
@JsonProperty("mechanism_comment")
public void setMechanismComment(Object mechanismComment) {
this.mechanismComment = mechanismComment;
}

/**
* 
* @return
* The mechanismOfAction
*/
@JsonProperty("mechanism_of_action")
public String getMechanismOfAction() {
return mechanismOfAction;
}

/**
* 
* @param mechanismOfAction
* The mechanism_of_action
*/
@JsonProperty("mechanism_of_action")
public void setMechanismOfAction(String mechanismOfAction) {
this.mechanismOfAction = mechanismOfAction;
}

/**
* 
* @return
* The molecularMechanism
*/
@JsonProperty("molecular_mechanism")
public Boolean getMolecularMechanism() {
return molecularMechanism;
}

/**
* 
* @param molecularMechanism
* The molecular_mechanism
*/
@JsonProperty("molecular_mechanism")
public void setMolecularMechanism(Boolean molecularMechanism) {
this.molecularMechanism = molecularMechanism;
}

/**
* 
* @return
* The moleculeChemblId
*/
@JsonProperty("molecule_chembl_id")
public String getMoleculeChemblId() {
return moleculeChemblId;
}

/**
* 
* @param moleculeChemblId
* The molecule_chembl_id
*/
@JsonProperty("molecule_chembl_id")
public void setMoleculeChemblId(String moleculeChemblId) {
this.moleculeChemblId = moleculeChemblId;
}

/**
* 
* @return
* The recordId
*/
@JsonProperty("record_id")
public Integer getRecordId() {
return recordId;
}

/**
* 
* @param recordId
* The record_id
*/
@JsonProperty("record_id")
public void setRecordId(Integer recordId) {
this.recordId = recordId;
}

/**
* 
* @return
* The selectivityComment
*/
@JsonProperty("selectivity_comment")
public Object getSelectivityComment() {
return selectivityComment;
}

/**
* 
* @param selectivityComment
* The selectivity_comment
*/
@JsonProperty("selectivity_comment")
public void setSelectivityComment(Object selectivityComment) {
this.selectivityComment = selectivityComment;
}

/**
* 
* @return
* The siteId
*/
@JsonProperty("site_id")
public Object getSiteId() {
return siteId;
}

/**
* 
* @param siteId
* The site_id
*/
@JsonProperty("site_id")
public void setSiteId(Object siteId) {
this.siteId = siteId;
}

/**
* 
* @return
* The targetChemblId
*/
@JsonProperty("target_chembl_id")
public String getTargetChemblId() {
return targetChemblId;
}

/**
* 
* @param targetChemblId
* The target_chembl_id
*/
@JsonProperty("target_chembl_id")
public void setTargetChemblId(String targetChemblId) {
this.targetChemblId = targetChemblId;
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
