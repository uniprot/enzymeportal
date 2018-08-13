/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.molecule;



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
"molecule_chembl_id",
"parent_chembl_id"
})
/**
 *
 * @author joseph
 */
public class MoleculeHierarchy {

@JsonProperty("molecule_chembl_id")
private String moleculeChemblId;
@JsonProperty("parent_chembl_id")
private String parentChemblId;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<>();

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
* The parentChemblId
*/
@JsonProperty("parent_chembl_id")
public String getParentChemblId() {
return parentChemblId;
}

/**
* 
* @param parentChemblId
* The parent_chembl_id
*/
@JsonProperty("parent_chembl_id")
public void setParentChemblId(String parentChemblId) {
this.parentChemblId = parentChemblId;
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