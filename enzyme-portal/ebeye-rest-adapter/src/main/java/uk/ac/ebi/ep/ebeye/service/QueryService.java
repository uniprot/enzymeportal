package uk.ac.ebi.ep.ebeye.service;

import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 * @param <T> SearchResult
 * @param <E> Entry
 */
public abstract class QueryService<T, E> {

    protected static final int RETRY_LIMIT = 1;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final EbeyeIndexProps ebeyeIndexProps;
    protected final RestTemplate restTemplate;

    public QueryService(EbeyeIndexProps ebeyeIndexProps, RestTemplate restTemplate) {
        Preconditions.checkArgument(restTemplate != null, "'restTemplate' must not be null");
        Preconditions.checkArgument(ebeyeIndexProps != null, "'ebeyeIndexProps' must not be null");

        this.ebeyeIndexProps = ebeyeIndexProps;
        this.restTemplate = restTemplate;

    }

    public T executeFirstQuery(String query, String queryTemplate, int resultSize, String endpoint, Class<T> clazz) {
        Observable<URI> urlRequest = generateUrlRequests(query, queryTemplate, resultSize, endpoint, 0, 1);

        return executeQueryRequest(urlRequest, clazz)
                .retry(RETRY_LIMIT)
                .doOnError((throwable -> logger.error("Error retrieving first response", throwable)))
                .onErrorReturn(throwable -> createEmptySearchResult(clazz))
                .toBlocking()
                .single();
    }

    private T createEmptySearchResult(Class<T> clazz) {
        try {
            T searchResult = clazz.newInstance();
            return searchResult;

        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("InstantiationException | IllegalAccessException error while creating empty search result", ex);
        }
        return null;
    }

    /**
     * Creates an {@link Observable} that holds reference to a request to the
     * Ebeye search service for the given reqUrl. The request will only be
     * executed once it is subscribed upon.
     *
     * @param reqUrlObs Observable creating request URL to call
     * @param clazz
     * @return the potential {@link EbeyeSearchResult} that results from the
     * call.
     */
    protected Observable<T> executeQueryRequest(Observable<URI> reqUrlObs, Class<T> clazz) {
        return reqUrlObs.map(reqUrl -> restTemplate.getForObject(reqUrl, clazz));
    }

    /**
     * Creates an Observable that generates url requests as needed by the
     * calling subscriber.
     *
     * @param start the start index of the URL
     * @param queryTemplate query url template e.g
     * %s?query=%s&size=%d&start=%d&fields=name&format=json
     * @param resultSize maxEbiSearchLimit
     * @param endpoint endpoint derived from ebeyeIndexProps
     * @param end the end index of the URL
     * @param query the query to search for
     * @return a URI that represents the request to make to the Ebeye search
     * service
     */
    protected Observable<URI> generateUrlRequests(String query, String queryTemplate, int resultSize, String endpoint, int start, int end) {
        return Observable.range(start, end)
                .map(index -> URI.create(String.format(queryTemplate, endpoint, query, resultSize, index * ebeyeIndexProps.getMaxEbiSearchLimit())));
    }

}
