/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.adapter.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeRestSearch {
    
    @JsonProperty("entries")
    private List<EbeyeSearchResult> results;
    
       public static void main(String args[]) {
        RestTemplate restTemplate = new RestTemplate();
        String url="http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query=cancer&format=json&size=20";
    

           
        EbeyeRestSearch result1 = restTemplate.getForObject(url, EbeyeRestSearch.class);
          List<EbeyeSearchResult> list = result1.getResults();
           System.out.println("List "+ list.size());
        
//        ResponseEntity<EbeyeSearchResult> result = restTemplate.exchange(url, 
//  HttpMethod.GET, null, EbeyeSearchResult.class);
        
        //EbeyeSearchResult result = restTemplate.getForObject(url, EbeyeSearchResult.class);
           
           System.out.println("Obj "+ list);
           
        for(EbeyeSearchResult result : list){
//        System.out.println("Acc:    " + result.getAcc());
//        System.out.println("Id:   " + result.getId());
//        System.out.println("Source:   " + result.getSource());
        }
        //System.out.println("Entries: " + result1);
    }

    public List<EbeyeSearchResult> getResults() {
        return results;
    }
       
       
       
}
