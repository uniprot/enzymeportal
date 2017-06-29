package uk.ac.ebi.ep.ebeye.service;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public abstract class ProteinQueryServiceImpl extends QueryService<ProteinGroupSearchResult, ProteinGroupEntry> {

    private int maxRetrievableHits;

    public ProteinQueryServiceImpl(EbeyeIndexProps ebeyeIndexProps, RestTemplate restTemplate, int maxHits) {
        super(ebeyeIndexProps, restTemplate);
        maxRetrievableHits = maxHits;
    }

    //@Override
    public Observable<ProteinGroupEntry> executeQuery(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");
        //final String queryTemplate = "%s?query=%s&size=%d&start=%d&fields=name,scientific_name,status&sort=status&reverse=true&format=json";
        final String queryTemplate = "%s?query=%s&size=%d&start=%d&fields=primary_accession&sort=_relevance&reverse=true&format=json";
        final int resultSize = ebeyeIndexProps.getMaxEbiSearchLimit();
        final String endpoint = ebeyeIndexProps.getProteinGroupSearchUrl();

        ProteinGroupSearchResult firstSearchResult = executeFirstQuery(query, queryTemplate, resultSize, endpoint, ProteinGroupSearchResult.class);

        int entryHitCount = firstSearchResult.getHitCount();

        logger.debug("Number of hits for search for [{}] : {}", query, firstSearchResult.getHitCount());

        Observable<ProteinGroupEntry> entries;

        if (entryHitCount <= ebeyeIndexProps.getMaxEbiSearchLimit()) {
            entries = Observable.from(firstSearchResult.getEntries());
        } else {
            entryHitCount = entryHitCount - firstSearchResult.getEntries().size();

            int maxEntriesToRetrieve = calculateMaxNumberOfHitsToRetrieve(entryHitCount);

            int totalPaginatedQueries
                    = (int) Math.ceil((double) maxEntriesToRetrieve / (double) ebeyeIndexProps.getMaxEbiSearchLimit());

            Observable<ProteinGroupEntry> remainingEntries = executeConcurrentPaginatedQueries(query, queryTemplate, resultSize, endpoint, 1, totalPaginatedQueries);

            entries = Observable.from(firstSearchResult.getEntries())
                    .mergeWith(remainingEntries)
                    .limit(maxRetrievableHits);
        }

        return entries;
    }

    private Observable<ProteinGroupEntry> executeConcurrentPaginatedQueries(String query, String queryTemplate, int resultSize, String endpoint, int requestStart, int requestEnd) {
        assert requestStart > -1 : "Start can not be a negative value";
        assert requestEnd > -1 : "End can not be a negative value";
        assert requestEnd >= requestStart : "End value can not be smaller than start value";

        final ForkJoinPool executorService = new ForkJoinPool();

        return generateUrlRequests(query, queryTemplate, resultSize, endpoint, requestStart, requestEnd)
                .flatMap(reqUrl
                        -> executeQueryRequest(Observable.just(reqUrl), ProteinGroupSearchResult.class)//.doOnEach(i -> System.out.println("exec :::  "+i))
                        .subscribeOn(Schedulers.from(executorService))
                        .map(ProteinGroupSearchResult::getEntries)
                        .flatMap(Observable::from)
                        .retry(RETRY_LIMIT)
                        .doOnError(throwable -> logger.error("Error executing request: {}", reqUrl, throwable))
                        .onExceptionResumeNext(Observable.empty()))
                .doOnUnsubscribe(executorService::shutdown);
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

    /**
     * Sends a query to the Ebeye search service and creates a response with the
     * accessions of the entries that fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use
     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
     * @return list of accessions that fulfill the query
     */
    public List<String> queryForUniquePrimaryAccessions(String query, int limit) {
        Preconditions.checkArgument(limit > 0, "Limit can not be less than 1");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> uniqueAccessions = queryForUniquePrimaryAccessions(query)
                .limit(limit)
                .toList()
                .toBlocking()
                .single();

        stopWatch.stop();
        logger.debug("Executing query:{}, took: {}", query, stopWatch.getTotalTimeSeconds() + " sec");

        return uniqueAccessions;
    }

    /**
     * Sends a query to the Ebeye search service and creates an
     * {@link Observable} which pushes the results as they are calculated. This
     * allows the results to be processed asynchronously by the client calling
     * the method.
     *
     * @param query the client query
     * @return Observable of accessions that fulfill the query
     */
    public Observable<String> queryForUniquePrimaryAccessions(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");

        Observable<String> uniqueAccessions;

        try {
            Observable<ProteinGroupEntry> distinctEntries = executeQuery(query).distinct();

            uniqueAccessions = getPrimaryAccessionsFromEntries(distinctEntries);
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            uniqueAccessions = Observable.empty();
        }

        return uniqueAccessions;
    }

    private Observable<String> getPrimaryAccessionsFromEntries(Observable<ProteinGroupEntry> accessionObservable) {
        return accessionObservable
                .map(ProteinGroupEntry::getPrimaryAccession)
                .distinct();
    }

}
