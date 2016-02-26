package uk.ac.ebi.ep.ebeye;

import java.util.*;
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
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 * REST client that communicates with the EBeye search web-service.
 *
 * @author joseph
 */
public class EbeyeRestService {
    public static final int NO_RESULT_LIMIT = Integer.MAX_VALUE;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AsyncRestTemplate asyncRestTemplate;
    private final EbeyeIndexUrl ebeyeIndexUrl;
    private final RestTemplate restTemplate;
    private final int maxEbiSearchLimit;
    private final int chunkSize;

    @Autowired
    public EbeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate,
            AsyncRestTemplate asyncRestTemplate, int maxEbiSearchLimit, int chunkSize) {
        Preconditions.checkArgument(ebeyeIndexUrl != null, "Index URL can't be null");
        Preconditions.checkArgument(restTemplate != null, "Synchronous REST template can't be null");
        Preconditions.checkArgument(asyncRestTemplate != null, "Asynchronous REST template can't be null");
        Preconditions.checkArgument(maxEbiSearchLimit > 0, "Max EBI seach limit can't be less than 1");
        Preconditions.checkArgument(chunkSize > 0, "Processable chunk sizes should be greater than 0");

        this.ebeyeIndexUrl = ebeyeIndexUrl;
        this.restTemplate = restTemplate;
        this.asyncRestTemplate = asyncRestTemplate;
        this.maxEbiSearchLimit = maxEbiSearchLimit;
        this.chunkSize=chunkSize;
    }

    /**
     * Generates an RPC call to the EBeye suggestion service in order to produce a list of suggestions for the client
     * to use.
     *
     * @param searchTerm the partial term that will be used by the EBeye service to generate suggestions
     * @return suggestions a list of suggestions generated from the EBeye service
     */
    public List<Suggestion> autocompleteSearch(String searchTerm) {
        String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

        EbeyeAutocomplete autocompleteResult = restTemplate.getForObject(url, EbeyeAutocomplete.class);

        return autocompleteResult.getSuggestions();
    }

    /**
     * Sends a query to the Ebeye search service and creates a response with the accessions of the entries that
     * fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use {@link #NO_RESULT_LIMIT}
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

            if (hitCount <= maxEbiSearchLimit) {
                accessions = extractUniqueAccessions(searchResult)
                        .stream()
                        .limit(limit == NO_RESULT_LIMIT ? hitCount : limit)
                        .collect(Collectors.toList());
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
                maxEbiSearchLimit,
                0);

        return restTemplate.getForObject(url, EbeyeSearchResult.class);
    }

    /**
     * Breaks down the client query into smaller chunks, submitting these to the server, and then merging the result.
     *
     * @param query the client query to run
     * @param hitCount the number of entries that the query is expected to hit
     * @param limit the maximum number of entries the client wants returned
     * @return a list of distinct accessions from the merged queries
     */
    private List<String> executeQueryInChunks(String query, int hitCount, int limit) {
        int totalPaginatedQueries = (int) Math.ceil((double) hitCount / (double) maxEbiSearchLimit);

        logger.debug("Possible number of paginated queries: {}", totalPaginatedQueries);

        Set<String> uniqueAccessions = new LinkedHashSet<>(limit);

        int startPage = 0;
        int endPage = Math.min(chunkSize, totalPaginatedQueries);

        while (startPage < totalPaginatedQueries && uniqueAccessions.size() < limit) {
            List<String> paginatedQueries = createPaginatedQueries(query, startPage, endPage);

            try {
                List<String> retrievedAccessions = processQueryChunks(paginatedQueries);
                uniqueAccessions.addAll(retrievedAccessions);
            } catch (RestClientException e) {
                logger.error("Error occurred whilst sending REST request:", e);
            }

            startPage = endPage;
            endPage = Math.min(endPage + chunkSize, totalPaginatedQueries);
        }

        return uniqueAccessions.stream().limit(limit).collect(Collectors.toList());
    }

    private List<String> processQueryChunks(List<String> paginatedQueries) throws RestClientException {
        List<ListenableFuture<ResponseEntity<EbeyeSearchResult>>> futureResults = paginatedQueries
                .stream()
                .map(this::getEbeyeSearchFutureResponse)
                .collect(Collectors.toList());

        return futureResults.stream()
                .map(this::extractAccessionsFromFuture)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> createPaginatedQueries(String query, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex)
                .mapToObj(index -> buildAccessionQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                        query,
                        maxEbiSearchLimit,
                        index * maxEbiSearchLimit))
                .collect(Collectors.toList());
    }

    private List<String> extractAccessionsFromFuture(ListenableFuture<ResponseEntity<EbeyeSearchResult>> future) {
        List<String> accessions;

        try {
            ResponseEntity<EbeyeSearchResult> response = future.get();
            EbeyeSearchResult searchResult = response.getBody();

            accessions = extractUniqueAccessions(searchResult);
        } catch (InterruptedException | ExecutionException | RestClientException e) {
            logger.warn("Unable to read response from future:", e);
            accessions = new ArrayList<>();
        }

        return accessions;
    }

    private List<String> extractUniqueAccessions(EbeyeSearchResult searchResult) {
        return searchResult.getEntries().stream()
                .map(Entry::getUniprotAccession)
                .distinct()
                .collect(Collectors.toList());
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
        String ebeyeAccessionQuery = "%s?query=%s&size=%d&start=%d&fields=name&format=json";

        return String.format(ebeyeAccessionQuery, endpoint, query, resultSize, start);
    }
}