/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class AccessionService {

    private final Logger logger = LoggerFactory.getLogger(AccessionService.class);

    //@Autowired
    private AsyncRestTemplate asyncRestTemplate;
    //@Autowired
    private EbeyeIndexProps ebeyeIndexUrl;
    //@Autowired
    private RestTemplate restTemplate;

    private final int maxEbiSearchLimit = 100;
    //Maximum number of entries that this service will ask from the EbeyeSearch
    private static final int MAX_HITS_TO_PROCESS = 7_000;

    public AccessionService(EbeyeIndexProps ebeyeIndexUrl, AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate) {
        this.asyncRestTemplate = asyncRestTemplate;
        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;
    }
    
        public List<String> queryForUniqueAccessions(String query, int limit) {
        // Preconditions.checkArgument(query != null, "Query can not be null");


        EbeyeSearchResult searchResult = synchronousQueryProtein(query);

        int synchronousHitCount = searchResult.getHitCount();
        int hitCount = synchronousHitCount < MAX_HITS_TO_PROCESS ? synchronousHitCount : MAX_HITS_TO_PROCESS;
        // hitCount = 20_000;
        
        logger.debug("Number of hits for search for [" + query + "] : " + hitCount);

        if (hitCount <= maxEbiSearchLimit) {

            return extractDistinctAccessionsFromSynchronousQuery(query);
        } else {

            int numIteration = (int) Math.ceil((double) hitCount / (double) maxEbiSearchLimit);
            return asynchronousQuery(query, numIteration, limit);
        }

    }

    public List<String> queryForUniqueAccessions(String ec, String searchTerm, int limit) {
        // Preconditions.checkArgument(query != null, "Query can not be null");
// String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?query="+query+" AND INTENZ:" + ec + "&fields=id,name,scientific_name,status&size=100&format=json";
        String query = searchTerm + " AND INTENZ:" + ec;
        
        return queryForUniqueAccessions(query, limit);

//        EbeyeSearchResult searchResult = synchronousQueryProtein(query);
//
//        int synchronousHitCount = searchResult.getHitCount();
//        int hitCount = synchronousHitCount < MAX_HITS_TO_PROCESS ? synchronousHitCount : MAX_HITS_TO_PROCESS;
//        // hitCount = 20_000;
//        //System.out.println("HITCOUNT " + hitCount);
//        logger.debug("Number of hits for search for [" + query + "] : " + hitCount);
//
//        if (hitCount <= maxEbiSearchLimit) {
//
//            return extractDistinctAccessionsFromSynchronousQuery(query);
//        } else {
//
//            int numIteration = (int) Math.ceil((double) hitCount / (double) maxEbiSearchLimit);
//            return asynchronousQuery(query, numIteration, limit);
//        }

    }

    private String buildQueryUrl(String endpoint, String query, int resultSize, int start) {
        String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=id,name,scientific_name,status&format=json";
        return String.format(ebeyeQueryUrl, endpoint, query, resultSize, start);
    }

    private List<String> buildUrlsForAsyncRequestProtein(String query, int numIteration) {

        List<String> urls = IntStream.range(0, numIteration)
                .parallel()
                .mapToObj(index -> buildQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                                query,
                                maxEbiSearchLimit,
                                index * maxEbiSearchLimit))
                .distinct()
                .collect(Collectors.toList());

        return Collections.unmodifiableList(urls);
    }

    private EbeyeSearchResult synchronousQueryProtein(String query) {

        String url = buildQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                query,
                maxEbiSearchLimit,
                0);
        return restTemplate.getForObject(url, EbeyeSearchResult.class);
    }

    private List<String> extractDistinctAccessionsFromSynchronousQuery(String query) {
        EbeyeSearchResult searchResult = synchronousQueryProtein(query);

        return searchResult
                .getEntries().stream().distinct()
                .map(e -> e.getUniprotAccession())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> asynchronousQuery(String query, int iteration, int limit) {

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("Number of availableProcessors : " + availableProcessors);
        //availableProcessors = 20;

        if (availableProcessors > 20) {//10

            final ForkJoinPool forkJoinPool = new ForkJoinPool(availableProcessors);
            CompletableFuture<List<String>> entries = CompletableFuture.supplyAsync(()
                    -> searchEbeyeDomainAccession(buildUrlsForAsyncRequestProtein(query, iteration), limit),
                    forkJoinPool
            );
            try {
                return entries.get();
            } catch (InterruptedException | ExecutionException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return searchEbeyeDomainAccession(buildUrlsForAsyncRequestProtein(query, iteration), limit);
    }

    private List<String> searchEbeyeDomainAccession(List<String> urls, int limit) {
        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        for (String url : urls) {
            ListenableFuture<ResponseEntity<EbeyeSearchResult>> future = asyncRestTemplate
                    .exchange(url, method, requestEntity, responseType);

            try {
                ResponseEntity<EbeyeSearchResult> results = future.get();
                List<String> acc = results.getBody()
                        .getEntries().stream().distinct().map(Entry::getUniprotAccession).distinct().limit(limit).collect(Collectors.toList());
                return acc;
            } catch (HttpClientErrorException ex) {
                logger.error(ex.getMessage(), ex);
            } catch (InterruptedException | ExecutionException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return null;
    }

//    protected void destroyHttpComponentsAsyncClientHttpRequestFactory() throws Exception {
//        httpComponentsAsyncClientHttpRequestFactory.destroy();
//    } 
}
