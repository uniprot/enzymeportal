package uk.ac.ebi.ep.ebeye;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;

import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 * REST client that communicates with the EBeye search web-service.
 *
 * @author joseph
 */
public class EbeyeRestService {
    public static final int NO_RESULT_LIMIT = Integer.MAX_VALUE;

    //Maximum number of entries that this service will ask from the EbeyeSearch
    private static final int MAX_HITS_TO_RETRIEVE = 20_000;

    private static final int MULTIPLE_QUERY_START_INDEX = 1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EbeyeIndexUrl ebeyeIndexUrl;
    private final RestTemplate restTemplate;

    private int maxRetrievableHits;

    public EbeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate) {
        Preconditions.checkArgument(ebeyeIndexUrl != null, "Index URL can't be null");
        Preconditions.checkArgument(restTemplate != null, "Synchronous REST template can't be null");

        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;
        this.maxRetrievableHits = MAX_HITS_TO_RETRIEVE;
    }

    /**
     * Generates an RPC call to the EBeye suggestion service in order to produce
     * a list of suggestions for the client to use.
     *
     * @param searchTerm the partial term that will be used by the EBeye service
     * to generate suggestions
     * @return suggestions a list of suggestions generated from the EBeye
     * service
     */
    public List<Suggestion> autocompleteSearch(String searchTerm) {
        String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

        EbeyeAutocomplete autocompleteResult = restTemplate.getForObject(url, EbeyeAutocomplete.class);

        return autocompleteResult.getSuggestions();
    }

    /**
     * Sends a query to the Ebeye search service and creates a response with the
     * accessions of the entries that fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use
     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
     * @return list of accessions that fulfill the query
     */
    public List<String> queryForUniqueAccessions(String query, int limit) {
        Preconditions.checkArgument(limit > 0, "Limit can not be less than 1");

        limit = calculateMaxNumberOfHitsToRetrieve(limit);

        return queryForUniqueAccessions(query)
                .limit(limit)
                .toList()
                .toBlocking()
                .single();
    }

    /**
     * Sends a query to the Ebeye search service and creates an {@link Observable} which pushes the results as they
     * are calculated. This allows the results to be processed asynchronously by the client calling the method.
     *
     * @param query the client query
     * @return Observable of accessions that fulfill the query
     */
    public Observable<String> queryForUniqueAccessions(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");

        Observable<String> uniqueAccessions;

        try {
            Observable<URI> firstUrlRequest = generateUrlRequests(0, 1, query);

            EbeyeSearchResult firstSearchResult = executeQueryRequest(firstUrlRequest)
                    .toBlocking()
                    .single();

            int hitCount = firstSearchResult.getHitCount();

            logger.debug("Number of hits for search for [{}] : {}", query, firstSearchResult.getHitCount());

            if (hitCount <= ebeyeIndexUrl.getMaxEbiSearchLimit()) {
                uniqueAccessions = Observable.from(firstSearchResult.getEntries())
                        .map(Entry::getUniprotAccession)
                        .distinct();
            } else {
                int newHitCount = hitCount - firstSearchResult.getEntries().size();

                Observable<Entry> remainingEntries = concurrentQueryRequests(query, newHitCount);

                Observable<Entry> distinctEntries = remainingEntries
                        .mergeWith(Observable.from(firstSearchResult.getEntries()))
                        .distinct();

                uniqueAccessions = getDistinctAccessionsFromEntries(distinctEntries);
            }
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            uniqueAccessions = Observable.empty();
        }

        return uniqueAccessions;
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

    private Observable<Entry> concurrentQueryRequests(String query, int hitCount) {
        hitCount = calculateMaxNumberOfHitsToRetrieve(hitCount);

        int totalPaginatedQueries = (int) Math.ceil((double) hitCount / (double) ebeyeIndexUrl.getMaxEbiSearchLimit());

        return reactiveRequests(MULTIPLE_QUERY_START_INDEX, totalPaginatedQueries, query);
    }

    private List<String> extractDistinctAccessionsUpToLimit(Collection<Entry> entries, int limit) {
        return entries.stream()
                .map(Entry::getUniprotAccession)
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Observable<Entry> reactiveRequests(int startIndex, int endIndex, String query) {
        ExecutorService executorService = Executors.newFixedThreadPool(ebeyeIndexUrl.getChunkSize());

        return generateUrlRequests(startIndex, endIndex, query)
                .flatMap(reqUrl ->
                        executeQueryRequest(Observable.just(reqUrl))
                                .subscribeOn(Schedulers.from(executorService))
                                .map(EbeyeSearchResult::getEntries)
                                .flatMap(Observable::from)
                                .onExceptionResumeNext(Observable.empty()))
                .doOnCompleted(executorService::shutdown);
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
        final String endpoint = ebeyeIndexUrl.getDefaultSearchIndexUrl();

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

    private Observable<String> getDistinctAccessionsFromEntries(Observable<Entry> accessionObservable) {
        return accessionObservable
                .map(Entry::getUniprotAccession)
                .distinct();
    }

    void setMaxRetrievableHits(int maxRetrievableHits) {
        assert maxRetrievableHits > 1 : "Number of hits to retrieve can not be less than 1";

        this.maxRetrievableHits = maxRetrievableHits;
    }
}