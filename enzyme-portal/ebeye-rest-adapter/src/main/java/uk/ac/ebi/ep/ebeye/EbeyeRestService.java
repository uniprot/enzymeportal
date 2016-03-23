/*
 Copyright (c) 2015 The European Bioinformatics Institute (EMBL-EBI).
 All rights reserved. Please see the file LICENSE
 in the root directory of this distribution.
 */
package uk.ac.ebi.ep.ebeye;

import com.aol.simple.react.stream.lazy.LazyReact;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static net.javacrumbs.futureconverter.springjava.FutureConverter.toCompletableFuture;
import org.slf4j.Logger;
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

    private final Logger logger = LoggerFactory.getLogger(EbeyeRestService.class);

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;
    @Autowired
    private EbeyeIndexUrl ebeyeIndexUrl;
    @Autowired
    private RestTemplate restTemplate;

    private static final int DEFAULT_EBI_SEARCH_LIMIT = 100;
    private static final int HITCOUNT = 7000;
    private static final int QUERY_LIMIT = 8000;

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
            logger.warn("Number of hits for search for " + query + " : " + searchResult.getHitCount());

            Set<String> accessions = new LinkedHashSet<>();

            if (!paginate) {
                searchResult.getEntries().stream().forEach((entry) -> {
                    accessions.add(entry.getUniprotAccession());
                });

                List<String> accessionList = accessions.stream().distinct().collect(Collectors.toList());
                logger.warn("Number of Accessions to be processed (Pagination = false) :  " + accessionList.size());
                return accessionList;

            }

            if (paginate) {

                int hitcount = searchResult.getHitCount();

                //for now limit hitcount to 7k
                if (hitcount > HITCOUNT) {
                    hitcount = HITCOUNT;
                }
                int resultLimit = 0;

                if (limit < 0) {
                    resultLimit = DEFAULT_EBI_SEARCH_LIMIT;
                }

                //for now limit results
                if (resultLimit > 0 && hitcount > resultLimit) {
                    hitcount = resultLimit;
                }

                int numIteration = hitcount / DEFAULT_EBI_SEARCH_LIMIT;

                List<String> accessionList = query(query, numIteration);
                logger.warn("Total Hitcount = " + hitcount + ",  Number of Accessions to be processed (when Pagination = true)  =  " + accessionList.size());
                return accessionList;

            }

        } catch (InterruptedException | NullPointerException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new ArrayList<>();
    }

    private EbeyeSearchResult queryEbeye(String query) throws InterruptedException, ExecutionException {

        List<String> ebeyeDomains = new ArrayList<>();
        ebeyeDomains.add(ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&query=");

        // get element as soon as it is available
        Optional<EbeyeSearchResult> result = ebeyeDomains.stream().map(base -> {
            String url = base + query;
            // open connection and fetch the result
            EbeyeSearchResult searchResult = null;
            try {
                searchResult = getEbeyeSearchResult(url.trim());

            } catch (InterruptedException | NullPointerException | ExecutionException ex) {
                logger.error(ex.getMessage(), ex);
            }

            return searchResult;

        }).findAny();

        return result.get();
    }

    @Deprecated
    private List<String> queryOld(String query, int iteration) throws InterruptedException, ExecutionException {
        final Set<String> accessions = new CopyOnWriteArraySet<>();

//        IntStream.rangeClosed(0, iteration).forEachOrdered(index -> {
//            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&start=" + index * DEFAULT_EBI_SEARCH_LIMIT + "&fields=name&query=" + query;
//
//            try {
//                EbeyeSearchResult ebeyeSearchResult = searchEbeyeDomain(url).get();
//
//                List<Entry> entries = ebeyeSearchResult.getEntries().stream().distinct().collect(Collectors.toList());
//                entries.stream().forEach((entry) -> {
//                    accessions.add(entry.getUniprotAccession());
//                });
//            } catch (InterruptedException | NullPointerException | ExecutionException ex) {
//                logger.error(ex.getMessage(), ex);
//            }
//
//        });
        final List<String> urls = IntStream.rangeClosed(0, iteration)
                .mapToObj(index -> ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&start=" + index * DEFAULT_EBI_SEARCH_LIMIT + "&fields=name&query=" + query)
                .collect(Collectors.toList());

        urls.parallelStream().forEach((url) -> {
            try {
                EbeyeSearchResult ebeyeSearchResult = searchEbeyeDomain(url).get();

                List<Entry> entries = ebeyeSearchResult.getEntries().stream().distinct().collect(Collectors.toList());
                entries.stream().forEach((entry) -> {
                    accessions.add(entry.getUniprotAccession());
                });
            } catch (InterruptedException | NullPointerException | ExecutionException ex) {
                logger.error(ex.getMessage(), ex);
            }
        });

        return accessions.stream().limit(QUERY_LIMIT).collect(Collectors.toList());

    }

    @Deprecated
    @Async
    private Future<EbeyeSearchResult> searchEbeyeDomain(String url) throws InterruptedException, ExecutionException {
        EbeyeSearchResult results = getEbeyeSearchResult(url);
        return new AsyncResult<>(results);
    }

    @Deprecated
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
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    private List<String> query(String query, int iteration) throws InterruptedException, ExecutionException {
        //final List<String> urls = new CopyOnWriteArrayList<>();
//        IntStream.rangeClosed(0, iteration).parallel().forEachOrdered(index -> {
//            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&start=" + index * DEFAULT_EBI_SEARCH_LIMIT + "&fields=name&query=" + query;
//
//            urls.add(url);
//
//        });
//
//        List<String> urlList = urls.stream().distinct().collect(Collectors.toList());

        final List<String> urls = IntStream.rangeClosed(0, iteration)
                .mapToObj(index -> ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&start=" + index * DEFAULT_EBI_SEARCH_LIMIT + "&fields=name&query=" + query)
                .collect(Collectors.toList());



        return searchEbeyeDomain(urls);

    }

    private List<String> createPaginatedQueries(String query, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex)
                .mapToObj(index -> buildAccessionQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                                query,
                                DEFAULT_EBI_SEARCH_LIMIT,
                                index * DEFAULT_EBI_SEARCH_LIMIT))
                .collect(Collectors.toList());
    }

    private String buildAccessionQueryUrl(String endpoint, String query, int resultSize, int start) {
        String ebeyeAccessionQuery = "%s?query=%s&size=%d&start=%d&fields=name&format=json";

        return String.format(ebeyeAccessionQuery, endpoint, query, resultSize, start);
    }

    public List<String> searchEbeyeDomain(List<String> urls) {

        List<String> accessions = new ArrayList<>();

        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);


        LazyReact lazyReact = new LazyReact().autoOptimizeOn().async();
  
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        ///System.out.println("POOL "+commonPool.getParallelism()); 

 
        List<Entry> result
         = 
        lazyReact.fromStream(urls.stream().map(x -> toCompletableFuture(asyncRestTemplate.exchange(x, method, requestEntity, responseType))))
                .map(ResponseEntity<EbeyeSearchResult>::getBody)
                .flatMap(x -> x.getEntries().stream().distinct()).limit(800).block();
                //.batchBySize(800)
                 // .onePer(1, TimeUnit.MICROSECONDS)
                //.peek(batch -> System.out.println("batched : " + 
                               // batch))
                //.forEach(this::save);


        
         accessions =result.stream().map(x->x.getUniprotAccession())
                .collect(Collectors.toList());
        
     

        return accessions.stream().distinct().collect(Collectors.toList());
    }

}
