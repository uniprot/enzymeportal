/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.molecule;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Source {
    
    @JsonProperty("mappings")
private List<Mapping> mappings = new ArrayList<Mapping>();
@JsonProperty("expression_host_scientific_name")
private String expressionHostScientificName;
@JsonProperty("expression_host_tax_id")
private Integer expressionHostTaxId;
@JsonProperty("organism_scientific_name")
private String organismScientificName;
@JsonProperty("tax_id")
private Integer taxId;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The mappings
*/
@JsonProperty("mappings")
public List<Mapping> getMappings() {
return mappings;
}

/**
* 
* @param mappings
* The mappings
*/
@JsonProperty("mappings")
public void setMappings(List<Mapping> mappings) {
this.mappings = mappings;
}

/**
* 
* @return
* The expressionHostScientificName
*/
@JsonProperty("expression_host_scientific_name")
public String getExpressionHostScientificName() {
return expressionHostScientificName;
}

/**
* 
* @param expressionHostScientificName
* The expression_host_scientific_name
*/
@JsonProperty("expression_host_scientific_name")
public void setExpressionHostScientificName(String expressionHostScientificName) {
this.expressionHostScientificName = expressionHostScientificName;
}

/**
* 
* @return
* The expressionHostTaxId
*/
@JsonProperty("expression_host_tax_id")
public Integer getExpressionHostTaxId() {
return expressionHostTaxId;
}

/**
* 
* @param expressionHostTaxId
* The expression_host_tax_id
*/
@JsonProperty("expression_host_tax_id")
public void setExpressionHostTaxId(Integer expressionHostTaxId) {
this.expressionHostTaxId = expressionHostTaxId;
}

/**
* 
* @return
* The organismScientificName
*/
@JsonProperty("organism_scientific_name")
public String getOrganismScientificName() {
return organismScientificName;
}

/**
* 
* @param organismScientificName
* The organism_scientific_name
*/
@JsonProperty("organism_scientific_name")
public void setOrganismScientificName(String organismScientificName) {
this.organismScientificName = organismScientificName;
}

/**
* 
* @return
* The taxId
*/
@JsonProperty("tax_id")
public Integer getTaxId() {
return taxId;
}

/**
* 
* @param taxId
* The tax_id
*/
@JsonProperty("tax_id")
public void setTaxId(Integer taxId) {
this.taxId = taxId;
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
