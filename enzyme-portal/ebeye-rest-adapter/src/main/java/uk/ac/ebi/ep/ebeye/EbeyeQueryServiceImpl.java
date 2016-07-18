package uk.ac.ebi.ep.ebeye;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 * Concrete implementation of the {@link EbeyeQueryService}.
 *
 * @author Ricardo Antunes
 */
public class EbeyeQueryServiceImpl implements EbeyeQueryService {
    private static final int MAX_RETRIEVABLE_ENTRIES = 10_000;

    private static final int RETRY_LIMIT = 1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EbeyeIndexProps ebeyeIndexProps;
    private final RestTemplate restTemplate;

    private int maxRetrievableHits;

    public EbeyeQueryServiceImpl(EbeyeIndexProps ebeyeIndexProps, RestTemplate restTemplate) {
        Preconditions.checkArgument(restTemplate != null, "'restTemplate' must not be null");
        Preconditions.checkArgument(ebeyeIndexProps != null, "'ebeyeIndexProps' must not be null");

        this.ebeyeIndexProps = ebeyeIndexProps;
        this.restTemplate = restTemplate;

        this.maxRetrievableHits = MAX_RETRIEVABLE_ENTRIES;
    }

    @Override
    public Observable<Entry> executeQuery(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");

        EbeyeSearchResult firstSearchResult = executeFirstQuery(query);

        int entryHitCount = firstSearchResult.getHitCount();

        logger.debug("Number of hits for search for [{}] : {}", query, firstSearchResult.getHitCount());

        Observable<Entry> entries;

        if (entryHitCount <= ebeyeIndexProps.getMaxEbiSearchLimit()) {
            entries = Observable.from(firstSearchResult.getEntries());
        } else {
            entryHitCount = entryHitCount - firstSearchResult.getEntries().size();

            int maxEntriesToRetrieve = calculateMaxNumberOfHitsToRetrieve(entryHitCount);

            int totalPaginatedQueries
                    = (int) Math.ceil((double) maxEntriesToRetrieve / (double) ebeyeIndexProps.getMaxEbiSearchLimit());

            Observable<Entry> remainingEntries = executeConcurrentPaginatedQueries(query, 1, totalPaginatedQueries);

            entries = Observable.from(firstSearchResult.getEntries())
                    .mergeWith(remainingEntries)
                    .limit(maxRetrievableHits);
        }

        return entries;
    }

    private Observable<Entry> executeConcurrentPaginatedQueries(String query, int requestStart, int requestEnd) {
        assert requestStart > -1 : "Start can not be a negative value";
        assert requestEnd > -1 : "End can not be a negative value";
        assert requestEnd >= requestStart : "End value can not be smaller than start value";

        int threadPoolSize = Math.max(ebeyeIndexProps.getChunkSize(), requestEnd - requestStart);
       // ExecutorService executorService = Executors.newWorkStealingPool();//.newFixedThreadPool(threadPoolSize);
        final ForkJoinPool executorService = new ForkJoinPool();
        AtomicInteger count = new AtomicInteger(0);
        return generateUrlRequests(query, requestStart, requestEnd)
                .flatMap(reqUrl
                        -> executeQueryRequest(Observable.just(reqUrl))
                        .subscribeOn(Schedulers.from(executorService))
                        .map(EbeyeSearchResult::getEntries)
                        .flatMap(Observable::from)
                        .retry(RETRY_LIMIT)
                        .doOnError(throwable -> logger.error("Error executing request: {}", reqUrl, throwable))
                        .onExceptionResumeNext(Observable.empty()))
                .doOnUnsubscribe(executorService::shutdown);
                //.doOnCompleted(executorService::shutdown);
    }

    private EbeyeSearchResult executeFirstQuery(String query) {
        Observable<URI> urlRequest = generateUrlRequests(query, 0, 1);

        return executeQueryRequest(urlRequest)//.doOnEach(i -> System.out.println("URL "+i))
                .retry(RETRY_LIMIT)
                .doOnError((throwable -> logger.error("Error retrieving first response", throwable)))
                .onErrorReturn(throwable -> createEmptySearchResult())
                .toBlocking()
                .single();
    }

    /**
     * Creates an Observable that generates url requests as needed by the
     * calling subscriber.
     *
     * @param start the start index of the URL
     * @param end the end index of the URL
     * @param query the query to search for
     * @return a URI that represents the request to make to the Ebeye search
     * service
     */
    private Observable<URI> generateUrlRequests(String query, int start, int end) {
        //final String ebeyeAccessionQuery = "%s?query=%s&size=%d&start=%d&fields=name&format=json";
        final String ebeyeAccessionQuery = "%s?query=%s&size=%d&start=%d&fields=id,name,scientific_name,status&format=json";
        final int resultSize = ebeyeIndexProps.getMaxEbiSearchLimit();
        final String endpoint = ebeyeIndexProps.getDefaultSearchIndexUrl();

        return Observable.range(start, end)
                .map(index
                        -> URI.create(String.format(ebeyeAccessionQuery, endpoint, query, resultSize,
                                        index * ebeyeIndexProps.getMaxEbiSearchLimit())));
    }

    /**
     * Creates an {@link Observable} that holds reference to a request to the
     * Ebeye search service for the given reqUrl. The request will only be
     * executed once it is subscribed upon.
     *
     * @param reqUrlObs Observable creating request URL to call
     * @return the potential {@link EbeyeSearchResult} that results from the
     * call.
     */
    private Observable<EbeyeSearchResult> executeQueryRequest(Observable<URI> reqUrlObs) {
        return reqUrlObs.map(reqUrl -> restTemplate.getForObject(reqUrl, EbeyeSearchResult.class));
    }

    private EbeyeSearchResult createEmptySearchResult() {
        EbeyeSearchResult searchResult = new EbeyeSearchResult();
        searchResult.setHitCount(0);
        searchResult.setEntries(Collections.emptyList());
        return searchResult;
    }

    /**
     * Method that defines the cutoff of entries to retrieve from the server.
     *
     * If the number of potential retrievable entries is above the
     * {@link #maxRetrievableHits} value, then the number of retrievable hits is
     * reduced to {@link #maxRetrievableHits}, otherwise return the value.
     *
     * @param value number of potential retrievable hits
     * @return the number of hits to retrieve.
     */
    private int calculateMaxNumberOfHitsToRetrieve(int value) {
        return value < maxRetrievableHits ? value : maxRetrievableHits;
    }

    void setMaxRetrievableHits(int maxRetrievableHits) {
        assert maxRetrievableHits > 1 : "Number of hits to retrieve can not be less than 1";

        this.maxRetrievableHits = maxRetrievableHits;
    }
}
