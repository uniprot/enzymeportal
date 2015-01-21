/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeFacetValues {

    @JsonProperty("label")
    private String label;
    @JsonProperty("value")
    private String value;

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
    
    
}
