package uk.ac.ebi.ep.ebeye;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;

import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;
import uk.ac.ebi.ep.ebeye.search.EnzymeEntry;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 * REST client that communicates with the EBeye search web-service retrieving
 * only enzyme centric data.
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricService {
    public static final int NO_RESULT_LIMIT = Integer.MAX_VALUE;

    private static final int MAX_HITS_TO_RETRIEVE = 20_000;
    private static final int MULTIPLE_QUERY_START_INDEX = 1;
    private static final int RETRY_LIMIT = 1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EbeyeIndexUrl ebeyeIndexUrl;
    private final RestTemplate restTemplate;

    private int maxRetrievableHits;

    public EnzymeCentricService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate) {
        Preconditions.checkArgument(restTemplate != null, "'restTemplate' must not be null");
        Preconditions.checkArgument(ebeyeIndexUrl != null, "'ebeyeIndexUrl' must not be null");

        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;

        this.maxRetrievableHits = MAX_HITS_TO_RETRIEVE;
    }

    public List<String> queryEbeyeForEcNumbers(String query, int limit) {
        Preconditions.checkArgument(query != null, "Query cannot be null");
        Preconditions.checkArgument(limit > 0, "Limit must be greater than 0");

        Observable<URI> firstUrlRequest = generateUrlRequests(0, 1, query);

        EbeyeSearchResult firstSearchResult = executeQueryRequest(firstUrlRequest)
                .retry(RETRY_LIMIT)
                .doOnError((throwable -> logger.error("Error retrieving first response", throwable)))
                .onErrorReturn(throwable -> createEmptySearchResult())
                .toBlocking()
                .single();

        int hitCount = firstSearchResult.getHitCount();

        logger.debug("Number of hits for search for [{}] : {}", query, firstSearchResult.getHitCount());

        List<String> uniqueECs;

        if (hitCount <= ebeyeIndexUrl.getMaxEbiSearchLimit()) {
            uniqueECs = firstSearchResult.getEntries().stream()
                    .map(Entry::getEc)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            int newHitCount = hitCount - firstSearchResult.getEntries().size();

            Observable<Entry> remainingEntries = concurrentQueryRequests(query, newHitCount);

            Observable<Entry> allEntries = remainingEntries
                    .mergeWith(Observable.from(firstSearchResult.getEntries()));

            uniqueECs = allEntries
                    .map(EnzymeEntry::getEc)
                    .distinct()
                    .limit(limit)
                    .toList()
                    .toBlocking()
                    .single();
        }

        return uniqueECs;
    }

    /**
     * Creates an Observable that generates url requests as needed by the calling subscriber.
     *
     * @param start the start index of the URL
     * @param end the end index of the URL
     * @param query the
     * @return a URI that represents the request to make to the Ebeye search service
     */
    private Observable<URI> generateUrlRequests(int start, int end, String query) {
        final String ebeyeAccessionQuery = "%s?query=%s&size=%d&start=%d&fields=name&format=json";
        final int resultSize = ebeyeIndexUrl.getMaxEbiSearchLimit();
        final String endpoint = ebeyeIndexUrl.getEnzymeCentricSearchUrl();

        return Observable.range(start, end)
                .map(index ->
                        URI.create(String.format(ebeyeAccessionQuery, endpoint, query, resultSize,
                                index * ebeyeIndexUrl.getMaxEbiSearchLimit())));
    }

    /**
     * Creates an {@link Observable} that holds reference to a request to the Ebeye search service for the given reqUrl.
     * The request will only be executed once it is subscribed upon.
     *
     * @param reqUrlObs Observable creating request URL to call
     * @return the potential {@link EbeyeSearchResult} that results from the call.
     */
    private Observable<EbeyeSearchResult> executeQueryRequest(Observable<URI> reqUrlObs) {
        return reqUrlObs.map(reqUrl -> restTemplate.getForObject(reqUrl, EbeyeSearchResult.class));
    }

    private Observable<Entry> concurrentQueryRequests(String query, int hitCount) {
        hitCount = calculateMaxNumberOfHitsToRetrieve(hitCount);

        int totalPaginatedQueries = (int) Math.ceil((double) hitCount / (double) ebeyeIndexUrl.getMaxEbiSearchLimit());

        return reactiveRequests(MULTIPLE_QUERY_START_INDEX, totalPaginatedQueries, query);
    }

    private int calculateMaxNumberOfHitsToRetrieve(int value) {
        return value < maxRetrievableHits ? value : maxRetrievableHits;
    }

    private Observable<Entry> reactiveRequests(int startIndex, int endIndex, String query) {
        ExecutorService executorService = Executors.newFixedThreadPool(ebeyeIndexUrl.getChunkSize());

        return generateUrlRequests(startIndex, endIndex, query)
                .flatMap(reqUrl ->
                        executeQueryRequest(Observable.just(reqUrl))
                                .subscribeOn(Schedulers.from(executorService))
                                .map(EbeyeSearchResult::getEntries)
                                .flatMap(Observable::from)
                                .retry(RETRY_LIMIT)
                                .doOnError(throwable -> logger.error("Error executing request: {}", reqUrl, throwable))
                                .onExceptionResumeNext(Observable.empty()))
                .doOnCompleted(executorService::shutdown);
    }

    private EbeyeSearchResult createEmptySearchResult() {
        EbeyeSearchResult searchResult = new EbeyeSearchResult();
        searchResult.setHitCount(0);
        searchResult.setEntries(Collections.emptyList());
        return searchResult;
    }

    void setMaxRetrievableHits(int maxRetrievableHits) {
        assert maxRetrievableHits > 1 : "Number of hits to retrieve can not be less than 1";

        this.maxRetrievableHits = maxRetrievableHits;
    }
}