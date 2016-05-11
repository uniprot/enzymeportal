package uk.ac.ebi.ep.ebeye;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
import rx.Observable;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EbeyeQueryService queryService;

    public EnzymeCentricService(EbeyeQueryService queryService) {
        Preconditions.checkArgument(queryService != null, "'EbeyeQueryService' must not be null");

        this.queryService = queryService;
    }

    /**
     * Sends a query to the Ebeye search service and creates a response with the
     * EC numbers of the entries that fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use
     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
     * @return list of EC numbers that fulfill the query
     */
    public List<String> queryEbeyeForEcNumbers(String query, int limit) {
        Preconditions.checkArgument(limit > 0, "Limit must be greater than 0");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> uniqueECs = queryEbeyeForEcNumbers(query)
                .limit(limit)
                .toList()
                .toBlocking()
                .single();

        stopWatch.stop();
        logger.debug("Executing query:{}, took: {}", query, stopWatch.getTotalTimeMillis());

        return uniqueECs;
    }

    /**
     * Sends a query to the Ebeye search service and creates an {@link Observable} which pushes the results as they
     * are calculated. This allows the results to be processed asynchronously by the client calling the method.
     *
     * @param query the client query
     * @return Observable of accessions that fulfill the query
     */
    public Observable<String> queryEbeyeForEcNumbers(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");

        Observable<String> uniqueAccessions;

        try {
            Observable<Entry> distinctEntries = queryService.executeQuery(query);

            uniqueAccessions = getDistinctEcNumbersFromEntries(distinctEntries);
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            uniqueAccessions = Observable.empty();
        }

        return uniqueAccessions;
    }

    private Observable<String> getDistinctEcNumbersFromEntries(Observable<Entry> entryObservable) {
        return entryObservable
                .map(EnzymeEntry::getEc)
                .distinct();
    }
}