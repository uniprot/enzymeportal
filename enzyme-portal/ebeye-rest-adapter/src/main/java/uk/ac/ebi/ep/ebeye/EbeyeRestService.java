/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

/**
 *
 * @author joseph
 */
public class EbeyeRestService  {
private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EbeyeRestService.class);
    /**
     * 
     * @param query user query
     * @return list of accessions
     */
    public List<String> queryEbeyeForAccessions(String query) {

        return queryEbeyeForAccessions(query, false, 0);
    }

    /**
     * 
     * @param query user query
     * @param paginate paginate ebeye service
     * @return  list of accessions
     */
    public List<String> queryEbeyeForAccessions(String query, boolean paginate) {
        return queryEbeyeForAccessions(query, paginate, 0);
    }

    /**
     * 
     * @param query
     * @param paginate
     * @param limit limit the number of results from Ebeye service.default is 100 and only to be used in pagination is true.
     * @return list of accessions
     */
    public List<String> queryEbeyeForAccessions(String query, boolean paginate, int limit) {

        EbeyeSearchResult searchResult = queryEbeye(query.trim());
        LOGGER.info("Number of hits for search for "+ query+ " : "+ searchResult.getHitCount());

       
        Set<String> accessions = new LinkedHashSet<>();

        List<String> accessionList = null;

        if (!paginate) {
            for (Entry entry : searchResult.getEntries()) {
                accessions.add(entry.getUniprot_accession());
            }

            accessionList = accessions.stream().distinct().collect(Collectors.toList());
 
        }

        if (paginate) {

            int hitcount = searchResult.getHitCount();

            if (limit < 0) {
                limit = 100;
            }

            //for now limit results
            if (limit > 0 && hitcount > limit) {
                hitcount = limit;
            }

            int numIteration = hitcount / 100;

            accessionList = query(query, numIteration);

        }

        LOGGER.info("Number of Accessions to be processed "+ accessionList.size());
        return accessionList;
    }

    private EbeyeSearchResult queryEbeye(String query) {
        List<String> ebeyeDomains = new ArrayList<String>() {
            {

                add("http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?format=json&size=100&query=");
                //add("http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?format=json&size=100&query=");
                //add("http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?format=json&size=100&fields=name&query=");
            }
        };

        // get element as soon as it is available
        Optional<EbeyeSearchResult> result = ebeyeDomains.stream().map((base) -> {
            String url = base + query;
            // open connection and fetch the result

            return getEbeyeSearchResult(url.trim());

        }).findAny();

        EbeyeSearchResult searchResult = result.get();

        return searchResult;
    }

    private List<String> query(String query, int iteration) {
        List<String> ebeyeDomains = new LinkedList<>();

        for (int index = 0; index <= iteration; index++) {

            String link = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?format=json&size=100&start=" + index * 100 + "&fields=name&query=";
            ebeyeDomains.add(link);
        }

        Set<String> accessions = new LinkedHashSet<>();
        // get element as soon as it is available
        List<EbeyeSearchResult> result = ebeyeDomains.stream().map((base) -> {
            String url = base + query;

            EbeyeSearchResult r = null;
            try {
                r = searchEbeyeDomain(url).get();

            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return r;

        }).collect(Collectors.toList());

        result.stream().map((ebeye) -> ebeye.getEntries().stream().distinct().collect(Collectors.toList())).forEach((entries) -> {
            entries.stream().forEach((entry) -> {
                accessions.add(entry.getUniprot_accession());
            });
        });

        List<String> accessionList = accessions.stream().distinct().collect(Collectors.toList());

        return accessionList;
    }

    @Async
    private Future<EbeyeSearchResult> searchEbeyeDomain(String url) throws InterruptedException {
        EbeyeSearchResult results = getEbeyeSearchResult(url);
        return new AsyncResult<>(results);
    }

    private EbeyeSearchResult getEbeyeSearchResult(String url) {
        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        //EbeyeSearchResult results = restTemplate().exchange(url, method, requestEntity, (Class<EbeyeSearchResult>) EbeyeSearchResult.class);//(url.trim(),method,requestEntity,responseType,"" );
        ListenableFuture<ResponseEntity<EbeyeSearchResult>> future = restTemplate()
                .exchange(url, method, requestEntity, responseType);

        // ResponseEntity<EbeyeSearchResult> results = null;
        try {
            ResponseEntity<EbeyeSearchResult> results = future.get();
            return results.getBody();
        } catch (InterruptedException | ExecutionException ex) {
           LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

    @Bean
    public AsyncRestTemplate restTemplate() {
        return new AsyncRestTemplate();
    }
}
