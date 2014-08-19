/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author joseph
 */
@Service
public class EbeyeService {

    private final RestTemplate restTemplate = new RestTemplate();
       // String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query=" + query + "&format=json&size=100";

    private EbeyeSearchResult getEbeyeSearchResult(String url) {
        EbeyeSearchResult results = restTemplate.getForObject(url, EbeyeSearchResult.class);
        return results;
    }

    @Async
    public Future<EbeyeSearchResult> searchEbeyeDomain(String url) throws InterruptedException {

        //EbeyeSearchResult results = restTemplate.getForObject(url, EbeyeSearchResult.class);
        //Thread.sleep(1000L);
        EbeyeSearchResult results = getEbeyeSearchResult(url);
        return new AsyncResult<>(results);
    }

    public EbeyeSearchResult query(String query) {
        List<String> ebeyeDomains = new ArrayList<String>() {
            {
                add("http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?format=json&size=100&query=");
               // add("http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?format=json&size=100&query=");
               // add("http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?format=json&size=100&query=");
            }
        };
        // get element as soon as it is available
        Optional<EbeyeSearchResult> result = ebeyeDomains.stream().parallel().map((base) -> {
            String url = base + query;
      // open connection and fetch the result
       
            return getEbeyeSearchResult(url);
//            EbeyeSearchResult r = null;
//            try {
//                r = searchEbeyeDomain(url).get();
//                
//            } catch (InterruptedException | ExecutionException ex) {
//                //Logger.getLogger(EbeyeService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return r;

        }).findAny();
        return result.get();
    }

}
