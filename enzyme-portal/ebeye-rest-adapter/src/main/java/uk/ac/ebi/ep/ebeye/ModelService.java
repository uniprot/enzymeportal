/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.List;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.model.Entry;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ModelService {

    //@Autowired
    //private PowerService powerService;
    private final RestTemplate restTemplate;// = new RestTemplate();

    public ModelService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param query searchTerm
     * @param startPage start page
     * @param pageSize page size
     * @param facets comma separated facets
     * @param facetCount number of facets to be returned
     * @return
     */
    public EBISearchResult getSearchResult(String query, int startPage, int pageSize, String facets, int facetCount) {
        //todo validate query paging, facets etc
        if(facetCount > 1000){
            facetCount = 1_000;
        }
        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
        System.out.println("facets "+ facets);
        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
            System.out.println("filtering ..");
            url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets=" + facets + "&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";

        }

        return getEbiSearchResult(url);
    }

    private EBISearchResult getEbiSearchResult(String url) {
        EBISearchResult results = restTemplate.getForObject(url.trim(), EBISearchResult.class);
        return results;
    }

    @Deprecated
    public EBISearchResult getModelSearchResult(String query, int page) {

//http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=sildenafil&facetcount=20&facets:TAXONOMY,OMIM,compound_type&size=100&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json
        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=20&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + page + "&size=10&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";

        //String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=10&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + page + "&size=10&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&format=json";
        EBISearchResult results = restTemplate.getForObject(url.trim(), EBISearchResult.class);
        return results;
    }

    @Deprecated
    public EBISearchResult filterSearchResult(String query, int page, String facets) {
//        System.out.println("facets "+ facets);
//String u = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=kinase&facetcount=10&facets=TAXONOMY:9606,disease_name:Leigh%20syndrome,disease_name:Glioma%202"; 
        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=20&facets=" + facets + "&start=" + page + "&size=10&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";

        EBISearchResult results = restTemplate.getForObject(url.trim(), EBISearchResult.class);
        return results;
    }

    @Deprecated
    public List<Entry> getEnzymeView(String query, int page) {

        return getModelSearchResult(query, page).getEntries();
    }

}
