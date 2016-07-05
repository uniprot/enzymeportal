/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.model.Entry;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ModelService {

    private final RestTemplate restTemplate;// = new RestTemplate();
    @Autowired
    private EbeyeIndexProps enzymeCentricProps;

    public ModelService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    private String buildQueryUrl(String endpoint, String query, int resultSize, int start) {
//        String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=name,status&format=json";
//
//        return String.format(ebeyeQueryUrl, endpoint, query, resultSize, start);
//    }
    private String buildQueryUrl(String endpoint, String query, int facetCount, String facets, int startPage, int pageSize) {
        //String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=name,status&format=json";
        String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY,OMIM,compound_type&compound_name&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";

             //String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" 
        // + facetCount + "&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
            // url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets=" + facets + "&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
            ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
return String.format(ebeyeQueryUrl, endpoint, query, facetCount, facets, startPage, pageSize);
        }
        //System.out.println(""+ ebeyeQueryUrl + " end "+ endpoint + " query "+ query + " facount "+ facetCount + " facets "+ facets + " start"+ startPage + " size "+ pageSize );
        return String.format(ebeyeQueryUrl, endpoint, query, facetCount, startPage, pageSize);
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
        System.out.println("start page "+ startPage);
        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");
        Preconditions.checkArgument(facets != null, "'facets' must not be null");
        Preconditions.checkArgument(facetCount > -1, "startPage can not be less than 0");

        if (facetCount > 1_000) {
            facetCount = 1_000;
        }
        //http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=3.4.24.55&facetcount=10&facets:TAXONOMY,OMIM,compound_type&compound_name&start=0&size=10&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json
        //System.out.println("FACETS "+ facets);
        
//        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
//        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
//            url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets=" + facets + "&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
//
//        }
        System.out.println("INDEX "+ enzymeCentricProps.getEnzymeCentricSearchUrl());

        return getEbiSearchResult(buildQueryUrl(enzymeCentricProps.getEnzymeCentricSearchUrl(), query, facetCount, facets, startPage, pageSize));
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
