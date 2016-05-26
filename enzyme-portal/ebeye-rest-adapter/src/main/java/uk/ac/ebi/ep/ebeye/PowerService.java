/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.aol.simple.react.stream.lazy.LazyReact;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static net.javacrumbs.futureconverter.springjava.FutureConverter.toCompletableFuture;
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
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.model.Protein;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */

public class PowerService {
   
    private final Logger logger = LoggerFactory.getLogger(ProteinCentricService.class);

    //@Autowired
    private AsyncRestTemplate asyncRestTemplate;
    //@Autowired
    private EbeyeIndexUrl ebeyeIndexUrl;
    //@Autowired
    private RestTemplate restTemplate;

    private final int maxEbiSearchLimit = 100;
    //Maximum number of entries that this service will ask from the EbeyeSearch
    private static final int MAX_HITS_TO_PROCESS = 2_00;

    public PowerService(EbeyeIndexUrl ebeyeIndexUrl,AsyncRestTemplate asyncRestTemplate,  RestTemplate restTemplate) {
        this.asyncRestTemplate = asyncRestTemplate;
        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;
    }
    
    
    
//       @Autowired
//    private HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory;

    public List<Protein> queryForUniqueProteins(String query, int limit) {
       // Preconditions.checkArgument(query != null, "Query can not be null");

        EbeyeSearchResult searchResult = synchronousQueryProtein(query);

        int synchronousHitCount = searchResult.getHitCount();
        int hitCount = synchronousHitCount < MAX_HITS_TO_PROCESS ? synchronousHitCount : MAX_HITS_TO_PROCESS;
       // hitCount = 20_000;
        //System.out.println("HITCOUNT " + hitCount);
        logger.debug("Number of hits for search for [" + query + "] : " + hitCount);

        if (hitCount <= maxEbiSearchLimit) {

            return extractDistinctProteinsFromSynchronousQuery(query);
        } else {

            int numIteration = (int) Math.ceil((double) hitCount / (double) maxEbiSearchLimit);
            return asynchronousQuery(query, numIteration, limit);
        }

    }

    private String buildQueryUrl(String endpoint, String query, int resultSize, int start) {
        String ebeyeQueryUrl = "%s?query=INTENZ:%s&size=%d&start=%d&fields=id,name,scientific_name,status&format=json";
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

    private List<Protein> extractDistinctProteinsFromSynchronousQuery(String query) {
        EbeyeSearchResult searchResult = synchronousQueryProtein(query);

        return searchResult
                .getEntries().stream().distinct()
                .map(ec -> ec.getProtein())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Protein> asynchronousQuery(String query, int iteration, int limit)  {

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("Number of availableProcessors : " + availableProcessors);
        //availableProcessors = 20;

        if (availableProcessors > 20) {//10

            final ForkJoinPool forkJoinPool = new ForkJoinPool(availableProcessors);
            CompletableFuture<List<Protein>> entries = CompletableFuture.supplyAsync(()
                    -> searchEbeyeDomainProtein(buildUrlsForAsyncRequestProtein(query, iteration), limit),
                    forkJoinPool
            );
            try {
                return entries.get();
            } catch (InterruptedException | ExecutionException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return searchEbeyeDomainProtein(buildUrlsForAsyncRequestProtein(query, iteration), limit);
    }
    
    
   private List<Protein> searchEbeyeDomainProtein(List<String> urls, int limit)  {
        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        for(String url : urls){
        ListenableFuture<ResponseEntity<EbeyeSearchResult>> future = asyncRestTemplate
                .exchange(url, method, requestEntity, responseType);

        try {
            ResponseEntity<EbeyeSearchResult> results = future.get();
            List<Protein> proteins = results.getBody()
                    .getEntries().stream().distinct().map(Entry::getProtein).distinct().limit(limit).collect(Collectors.toList());
            return proteins;
        } catch (HttpClientErrorException ex) {
            logger.error(ex.getMessage(), ex);
        }   catch (InterruptedException | ExecutionException ex) {
              logger.error(ex.getMessage(), ex); 
            }
        }

        return null;
    }

    private List<Protein> searchEbeyeDomainProteinXXX(List<String> urls, int limit) {

        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        LazyReact lazyReact = new LazyReact();

//       List<Protein> result =  lazyReact
//                .fromStream(urls.stream()
//                        .map(url -> toCompletableFuture(asyncRestTemplate.exchange(url, method, requestEntity, responseType))))
//                .map(ResponseEntity<EbeyeSearchResult>::getBody)
//                .map(EbeyeSearchResult::getEntries)
//                .flatMap(Collection::stream).distinct()
//                .map(entry -> entry.getProtein())
//                .limit(limit)
//                .collect(Collectors.toList());
       
       
             List<Entry> entries =  lazyReact
                .fromStream(urls.stream()
                        .map(url -> toCompletableFuture(asyncRestTemplate.exchange(url, method, requestEntity, responseType))))
                .map(ResponseEntity<EbeyeSearchResult>::getBody)
                .map(EbeyeSearchResult::getEntries)
                .flatMap(Collection::stream).distinct()
                //.map(entry -> entry.getProtein())
                .limit(limit)
                .collect(Collectors.toList()); 
             List<Protein> result = new LinkedList<>();
             for(Entry e : entries){
                 Protein p = new Protein(e.getUniprotAccession(), e.getTitle(), e.getScientificName().stream().findFirst().orElse(""));
                 result.add(p);
             }
       
       

//            try {
//            destroyHttpComponentsAsyncClientHttpRequestFactory();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        }
        return result;
        
    }
    
//    protected void destroyHttpComponentsAsyncClientHttpRequestFactory() throws Exception {
//        httpComponentsAsyncClientHttpRequestFactory.destroy();
//    } 
}
