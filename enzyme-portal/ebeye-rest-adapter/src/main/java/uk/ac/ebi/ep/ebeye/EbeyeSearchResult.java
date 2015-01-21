/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeSearchResult {

    @JsonProperty("entries")
    private List<UniProtDomain> uniProtDomains;

    @JsonProperty("facets")
    private List<EbeyeFacets> facets;

    private final List<String> ecNumbers;

    public EbeyeSearchResult() {
        ecNumbers = new LinkedList<>();
    }

    public List<UniProtDomain> getUniProtDomains() {
        if (uniProtDomains == null) {
            uniProtDomains = new ArrayList<>();
        }
        return uniProtDomains;
    }

    public List<EbeyeFacets> getFacets() {
        if (facets == null) {
            facets = new ArrayList<>();
        }

        return facets;
    }

    private void computeEcNumbers() {

        getFacets().stream().filter((facet) -> (facet.getId().equalsIgnoreCase("ec_number"))).forEach((facet) -> {
            facet.getFacetValues().stream().forEach((facetValue) -> {
                ecNumbers.add(facetValue.getValue());
            });
        });
    }

    public List<String> getEcNumbers() {
        computeEcNumbers();
        return ecNumbers;
    }

    //for testing purpose only
    public void setUniProtDomains(List<UniProtDomain> uniProtDomains) {
        this.uniProtDomains = uniProtDomains;
    }
    
    

}
