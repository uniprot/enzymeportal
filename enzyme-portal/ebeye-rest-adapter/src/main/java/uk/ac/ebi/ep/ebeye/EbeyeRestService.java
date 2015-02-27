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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 *
 * @author joseph
 */
public class EbeyeRestService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EbeyeRestService.class);

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;
    @Autowired
    private EbeyeIndexUrl ebeyeIndexUrl;
    @Autowired
    private RestTemplate restTemplate;

    /**
     *
     * @param searchTerm
     * @return suggestions
     */
    public List<Suggestion> ebeyeAutocompleteSearch(String searchTerm) {
        String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

        EbeyeAutocomplete autocomplete = restTemplate.getForObject(url.trim(), EbeyeAutocomplete.class);

        return autocomplete.getSuggestions();

    }

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
     * @return list of accessions
     */
    public List<String> queryEbeyeForAccessions(String query, boolean paginate) {
        return queryEbeyeForAccessions(query, paginate, 0);
    }

    /**
     *
     * @param query
     * @param paginate
     * @param limit limit the number of results from Ebeye service. default is
     * 100 and only used when pagination is true.
     * @return list of accessions
     */
    public List<String> queryEbeyeForAccessions(String query, boolean paginate, int limit) {

        try {
            EbeyeSearchResult searchResult = queryEbeye(query.trim());
            LOGGER.warn("Number of hits for search for " + query + " : " + searchResult.getHitCount());

            Set<String> accessions = new LinkedHashSet<>();

            if (!paginate) {
                for (Entry entry : searchResult.getEntries()) {
                    accessions.add(entry.getUniprot_accession());
                }

                List<String> accessionList = accessions.stream().distinct().collect(Collectors.toList());
                LOGGER.warn("Number of Accessions to be processed (Pagination = false) :  " + accessionList.size());
                return accessionList;

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

                List<String> accessionList = query(query, numIteration);
                LOGGER.warn("Number of Accessions to be processed (Pagination = true)  :  " + accessionList.size());
                return accessionList;

            }

        } catch (InterruptedException | NullPointerException | ExecutionException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ArrayList<>();
    }

    private EbeyeSearchResult queryEbeye(String query) throws InterruptedException, ExecutionException {
        List<String> ebeyeDomains = new ArrayList<String>() {
            {
                add(ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&query=");

            }
        };

        // get element as soon as it is available
        Optional<EbeyeSearchResult> result = ebeyeDomains.stream().map(base -> {
            String url = base + query;
            // open connection and fetch the result
            EbeyeSearchResult searchResult = null;
            try {
                searchResult = getEbeyeSearchResult(url.trim());
                //return getEbeyeSearchResult(url.trim());
            } catch (InterruptedException | NullPointerException | ExecutionException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

            return searchResult;

        }).findAny();

        EbeyeSearchResult searchResult = result.get();

        return searchResult;
    }

    private List<String> query(String query, int iteration) throws InterruptedException, ExecutionException {
        List<String> ebeyeDomains = new LinkedList<>();

        for (int index = 0; index <= iteration; index++) {

            String link = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&start=" + index * 100 + "&fields=name&query=";
            ebeyeDomains.add(link);
        }

        Set<String> accessions = new LinkedHashSet<>();
        // get element as soon as it is available
        List<EbeyeSearchResult> result = ebeyeDomains.stream().map(base -> {
            String url = base + query;

            EbeyeSearchResult r = null;
            try {
                r = searchEbeyeDomain(url).get();

            } catch (InterruptedException | NullPointerException | ExecutionException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return r;

        }).collect(Collectors.toList());

        result.stream().map(ebeye -> ebeye.getEntries().stream().distinct().collect(Collectors.toList())).forEach(entries -> {
            entries.stream().forEach(entry -> {
                accessions.add(entry.getUniprot_accession());
            });
        });

        List<String> accessionList = accessions.stream().distinct().collect(Collectors.toList());

        return accessionList;
    }

    @Async
    private Future<EbeyeSearchResult> searchEbeyeDomain(String url) throws InterruptedException, ExecutionException {
        EbeyeSearchResult results = getEbeyeSearchResult(url);
        return new AsyncResult<>(results);
    }

    private EbeyeSearchResult getEbeyeSearchResult(String url) throws InterruptedException, ExecutionException {
        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        ListenableFuture<ResponseEntity<EbeyeSearchResult>> future = asyncRestTemplate
                .exchange(url, method, requestEntity, responseType);

        try {
            ResponseEntity<EbeyeSearchResult> results = future.get();
            return results.getBody();
        } catch (HttpClientErrorException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

}
