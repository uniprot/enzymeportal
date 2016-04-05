/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.aol.simple.react.stream.lazy.LazyReact;
import java.util.Collection;
import java.util.Collections;
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
import org.springframework.util.Assert;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 * REST client that communicates with the EBeye search web-service retrieving
 * only enzyme centric data.
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricService {

    private final Logger logger = LoggerFactory.getLogger(EnzymeCentricService.class);

    private final AsyncRestTemplate asyncRestTemplate;

    private final EbeyeIndexUrl ebeyeIndexUrl;

    private final RestTemplate restTemplate;
    //Maximum number of entries that this service will ask from the EbeyeSearch
    private final int MAX_HITS_TO_PROCESS = 4;

    public EnzymeCentricService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate, AsyncRestTemplate asyncRestTemplate) {
        Assert.notNull(restTemplate, "'restTemplate' must not be null");
        Assert.notNull(asyncRestTemplate, "'asyncRestTemplate' must not be null");
        Assert.notNull(ebeyeIndexUrl, "'ebeyeIndexUrl' must not be null");
        this.asyncRestTemplate = asyncRestTemplate;
        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;
    }

    public List<String> queryEbeyeForEcNumbers(String query, int limit) throws InterruptedException, ExecutionException {
        Assert.notNull(query, "Query cannot be null");
        Preconditions.checkArgument(limit > 0, "Limit must be greater than 0");
        Assert.isTrue(limit > 0, "Limit must be greater than 0");
        EbeyeSearchResult searchResult = synchronousQuery(query);

        int synchronousHitCount = searchResult.getHitCount();
        int hitCount = synchronousHitCount < MAX_HITS_TO_PROCESS ? synchronousHitCount : MAX_HITS_TO_PROCESS;
        //hitCount = 20_000;

        logger.debug("Number of hits for search for [" + query + "] : " + hitCount);

        if (hitCount <= ebeyeIndexUrl.getMaxEbiSearchLimit()) {

            return extractDistinctEcNumbersFromSynchronousQuery(query);
        } else {

            int numIteration = (int) Math.ceil((double) hitCount / (double) ebeyeIndexUrl.getMaxEbiSearchLimit());
            return asynchronousQuery(query, numIteration, limit);
        }

    }

    private EbeyeSearchResult synchronousQuery(String query) {
        String url = buildQueryUrl(ebeyeIndexUrl.getEnzymeCentricSearchUrl(),
                query,
                ebeyeIndexUrl.getMaxEbiSearchLimit(),
                0);

        return restTemplate.getForObject(url, EbeyeSearchResult.class);
    }

    private List<String> extractDistinctEcNumbersFromSynchronousQuery(String query) {
        EbeyeSearchResult searchResult = synchronousQuery(query);
        return searchResult
                .getEntries().stream()
                .map(ec -> (ec.getEc()))
                .distinct()
                .collect(Collectors.toList());

    }

    private String buildQueryUrl(String endpoint, String query, int resultSize, int start) {
        String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=name&format=json";

        return String.format(ebeyeQueryUrl, endpoint, query, resultSize, start);
    }

    private List<String> buildUrlsForAsyncRequest(String query, int numIteration) {
        List<String> urls = IntStream.range(0, numIteration)
                .parallel()
                .mapToObj(index -> buildQueryUrl(ebeyeIndexUrl.getEnzymeCentricSearchUrl(),
                                query,
                                ebeyeIndexUrl.getMaxEbiSearchLimit(),
                                index * ebeyeIndexUrl.getMaxEbiSearchLimit()))
                .distinct()
                .collect(Collectors.toList());
        return Collections.unmodifiableList(urls);

    }

    private List<String> asynchronousQuery(String query, int iteration, int limit) throws InterruptedException, ExecutionException {

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("Number of availableProcessors : " + availableProcessors);
        //availableProcessors = 20;

        if (availableProcessors > 10) {

            return executeUsingFJP(availableProcessors, query, iteration, limit).get();
        }

        return searchEbeyeDomain(buildUrlsForAsyncRequest(query, iteration), limit);
    }

    private CompletableFuture<List<String>> executeUsingFJP(int availableProcessors, String query, int iteration, int limit) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool(availableProcessors);
        CompletableFuture<List<String>> completableFutureEntries = CompletableFuture.supplyAsync(()
                -> searchEbeyeDomain(buildUrlsForAsyncRequest(query, iteration), limit),
                forkJoinPool
        );
        return completableFutureEntries;
    }

    private List<String> searchEbeyeDomain(List<String> urls, int limit) {

        HttpMethod method = HttpMethod.GET;

        // Define response type
        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        LazyReact lazyReact = new LazyReact();

        return lazyReact
                .fromStream(urls.stream()
                        .map(url -> toCompletableFuture(asyncRestTemplate.exchange(url, method, requestEntity, responseType))))
                .map(ResponseEntity<EbeyeSearchResult>::getBody)
                .map(EbeyeSearchResult::getEntries)
                .flatMap(Collection::stream).distinct()
                .map(entry -> entry.getEc())
                .limit(limit)
                .collect(Collectors.toList());

    }

}
