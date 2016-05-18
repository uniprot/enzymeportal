/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import java.util.List;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ModelService {

    private final RestTemplate restTemplate = new RestTemplate();

    public EBISearchResult getModelSearchResult(String query) {

        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=20&facets:TAXONOMY,OMIM,compound_type&size=100&fields=id,name,description,acc,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
        EBISearchResult results = restTemplate.getForObject(url.trim(), EBISearchResult.class);
        return results;
    }
    
    public List<Entry> getEnzymeView(String query){
        
        return getModelSearchResult(query).getEntries();
    }

}
