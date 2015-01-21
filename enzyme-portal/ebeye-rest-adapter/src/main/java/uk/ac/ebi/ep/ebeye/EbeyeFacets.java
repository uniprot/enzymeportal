/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeFacets {

    @JsonProperty("id")
    private String id;
    @JsonProperty("label")
    private String label;

    @JsonProperty("facetValues")
    private List<EbeyeFacetValues> facetValues;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public List<EbeyeFacetValues> getFacetValues() {
        return facetValues;
    }
    
    

}
