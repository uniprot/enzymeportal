package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AsyncRestTemplate asyncRestTemplate;
    private final EbeyeIndexUrl ebeyeIndexUrl;
    private final RestTemplate restTemplate;

    private int maxRetrievableHits;
    @Autowired
    private HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory;

    public EbeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate,
            AsyncRestTemplate asyncRestTemplate) {
        Preconditions.checkArgument(ebeyeIndexUrl != null, "Index URL can't be null");
        Preconditions.checkArgument(restTemplate != null, "Synchronous REST template can't be null");
        Preconditions.checkArgument(asyncRestTemplate != null, "Asynchronous REST template can't be null");

        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;
        this.asyncRestTemplate = asyncRestTemplate;
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
     * {@link #NO_RESULT_LIMIT}
     * @return list of accessions that fulfill the query
     */
    public List<String> queryForUniqueAccessions(String query, int limit) {
        Preconditions.checkArgument(query != null, "Query can not be null");
        Preconditions.checkArgument(limit > 0, "Limit can not be less than 1");

        List<String> accessions;

        try {
            EbeyeSearchResult searchResult = synchronousQuery(query);

            int hitCount = searchResult.getHitCount();

            logger.debug("Number of hits for search for [" + query + "] : " + searchResult.getHitCount());

            if (hitCount <= ebeyeIndexUrl.getMaxEbiSearchLimit()) {
                accessions = extractDistinctAccessionsUpToLimit(searchResult.getEntries(),
                        limit == NO_RESULT_LIMIT ? hitCount : limit);
            } else {
                accessions = executeQueryInChunks(query, hitCount, limit);
            }

            logger.debug("Total amount of returned accessions: " + accessions.size());
        } catch (RestClientException | InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
            accessions = new ArrayList<>();
        }

        return accessions;
    }

    private EbeyeSearchResult synchronousQuery(String query)
            throws InterruptedException, ExecutionException, RestClientException {
        String url = buildAccessionQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                query,
                ebeyeIndexUrl.getMaxEbiSearchLimit(),
                0);

        return restTemplate.getForObject(url, EbeyeSearchResult.class);
    }

    /**
     * Method that defines the cutoff of entries to retrieve from the server.
     *
     * If the number of potential retrievable entries is above the
     * {@link #maxRetrievableHits} value, then the number of retrievable hits is
     * reduced to {@link #maxRetrievableHits}, otherwise return the hitCount.
     *
     * @param hitCount number of potential retrievable hits
     * @return the number of hits to retrieve.
     */
    private int calculateMaxNumberOfHitsToRetrieve(int hitCount, int maxRetrievableHits) {
        return hitCount < maxRetrievableHits ? hitCount : maxRetrievableHits;
    }

    /**
     * Breaks down the client query into smaller chunks, submitting these to the
     * server, and then merging the result.
     *
     * @param query the client query to run
     * @param hitCount the number of entries that the query is expected to hit
     * @param limit the maximum number of entries the client wants returned
     * @return a list of distinct accessions from the merged queries
     */
    private List<String> executeQueryInChunks(String query, int hitCount, int limit) {
        hitCount = calculateMaxNumberOfHitsToRetrieve(hitCount, maxRetrievableHits);

        int totalPaginatedQueries = (int) Math.ceil((double) hitCount / (double) ebeyeIndexUrl.getMaxEbiSearchLimit());

        logger.debug("Possible number of paginated queries: {}", totalPaginatedQueries);

        Set<Entry> uniqueEntries = new HashSet<>(limit > hitCount ? hitCount : limit);

        int startPage = 0;
        int endPage = Math.min(ebeyeIndexUrl.getChunkSize(), totalPaginatedQueries);

        while (startPage < totalPaginatedQueries && uniqueEntries.size() < limit) {
            List<String> paginatedQueries = createPaginatedQueries(query, startPage, endPage);

            try {
                List<Entry> retrievedEntries = processQueryChunks(paginatedQueries);
                uniqueEntries.addAll(retrievedEntries);
            } catch (RestClientException e) {
                logger.error("Error occurred whilst sending REST request", e);
            }

            startPage = endPage;
            endPage = Math.min(endPage + ebeyeIndexUrl.getChunkSize(), totalPaginatedQueries);
        }

        return extractDistinctAccessionsUpToLimit(uniqueEntries, limit);
    }

    private List<Entry> processQueryChunks(List<String> paginatedQueries) throws RestClientException {
        List<ListenableFuture<ResponseEntity<EbeyeSearchResult>>> futureResults = paginatedQueries
                .stream()
                .map(this::getEbeyeSearchFutureResponse)
                .collect(Collectors.toList());

        return futureResults.stream()
                .map(this::extractEntriesFromFuture)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> extractDistinctAccessionsUpToLimit(Collection<Entry> entries, int limit) {
        return entries.stream()
                .map(Entry::getUniprotAccession)
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<String> createPaginatedQueries(String query, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex)
                .mapToObj(index -> buildAccessionQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                                query,
                                ebeyeIndexUrl.getMaxEbiSearchLimit(),
                                index * ebeyeIndexUrl.getMaxEbiSearchLimit()))
                .collect(Collectors.toList());
    }

    private List<Entry> extractEntriesFromFuture(ListenableFuture<ResponseEntity<EbeyeSearchResult>> future) {
        List<Entry> entries;

        try {
            ResponseEntity<EbeyeSearchResult> response = future.get();
            EbeyeSearchResult searchResult = response.getBody();

            entries = searchResult.getEntries();
        } catch (InterruptedException | ExecutionException | RestClientException e) {
            logger.warn("Unable to read response from future:", e);
            entries = new ArrayList<>();
        }

        return entries;
    }

    //TODO (shutdown at the end of all requests)
    protected void destroy() throws Exception {
        httpComponentsAsyncClientHttpRequestFactory.destroy();
    }

    private ListenableFuture<ResponseEntity<EbeyeSearchResult>> getEbeyeSearchFutureResponse(String queryUrl)
            throws RestClientException {

        assert queryUrl != null : "URL to send to Ebeye search service can't be null";

        HttpMethod method = HttpMethod.GET;

        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);

        return asyncRestTemplate.exchange(queryUrl, method, requestEntity, responseType);
    }

    private String buildAccessionQueryUrl(String endpoint, String query, int resultSize, int start) {
        String ebeyeAccessionQuery = "%s?query=%s&size=%d&start=%d&fields=name,status&format=json";

        return String.format(ebeyeAccessionQuery, endpoint, query, resultSize, start);
    }

    void setMaxRetrievableHits(int maxRetrievableHits) {
        assert maxRetrievableHits > 1 : "Number of hits to retrieve can not be less than 1";

        this.maxRetrievableHits = maxRetrievableHits;
    }
}
