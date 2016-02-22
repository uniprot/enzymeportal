package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public static final int NO_RESULT_LIMIT = 0;

    private static final int DEFAULT_EBI_SEARCH_LIMIT = 100;
    private static final int QUERY_LIMIT = 800;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Autowired
    private EbeyeIndexUrl ebeyeIndexUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Generates an RPC call to the EBeye suggestion service in order to produce a list of suggestions for the client
     * to use.
     *
     * @param searchTerm the partial term that will be used by the EBeye service to generate suggestions
     * @return suggestions a list of suggestions generated from the EBeye service
     */
    public List<Suggestion> ebeyeAutocompleteSearch(String searchTerm) {
        String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

        EbeyeAutocomplete autocomplete = restTemplate.getForObject(url, EbeyeAutocomplete.class);

        return autocomplete.getSuggestions();
    }

    /**
     * Sends a query to the Ebeye search service and creates a response with the accessions of the entries that
     * fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use {@link #NO_RESULT_LIMIT}
     * @return list of accessions that fulfill the query
     */
    public List<String> queryEbeyeForUniqueAccessions(String query, int limit) {
        List<String> accessions;

        try {
            EbeyeSearchResult searchResult = queryEbeye(query);

            int hitcount = searchResult.getHitCount();

            logger.debug("Number of hits for search for [" + query + "] : " + searchResult.getHitCount());

            if (hitcount < DEFAULT_EBI_SEARCH_LIMIT) {
                accessions = searchResult.getEntries().stream()
                        .map(Entry::getUniprotAccession)
                        .distinct()
                        .limit(limit)
                        .collect(Collectors.toList());
            } else {
                accessions = partitionQuery(query, hitcount, limit);
            }

            logger.debug("Total amount of returned accessions: " + accessions.size());
        } catch (InterruptedException | NullPointerException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
            accessions = new ArrayList<>();
        }

        return accessions;
    }

    private EbeyeSearchResult queryEbeye(String query) throws InterruptedException, ExecutionException {
        String url = buildAccessionQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                query,
                DEFAULT_EBI_SEARCH_LIMIT,
                0);

        ListenableFuture<ResponseEntity<EbeyeSearchResult>> searchResult = getEbeyeSearchFutureResponse(url);

        return searchResult.get().getBody();
    }

    /**
     * Breaks down the client query into smaller chunks, submitting these to the server, and then merging the result.
     *
     * @param query the client query to run
     * @param hitcount the number of entries that the query is expected to hit
     * @param limit the maximum number of entries the client wants returned
     * @return a list of distinct accessions from the merged queries
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private List<String> partitionQuery(String query, int hitcount, int limit)
            throws InterruptedException, ExecutionException {
        int numQueries = hitcount / DEFAULT_EBI_SEARCH_LIMIT;

        List<String> paginatedQueries = createPaginatedQueries(query, numQueries);

        List<ListenableFuture<ResponseEntity<EbeyeSearchResult>>> futureResults = paginatedQueries
                .stream()
                .map(this::getEbeyeSearchFutureResponse)
                .collect(Collectors.toList());

        return futureResults.stream()
                .map(this::extractAccessionsFromFuture)
                .flatMap(Collection::stream)
                .distinct()
                .limit(limit == NO_RESULT_LIMIT ? Integer.MAX_VALUE : limit)
                .collect(Collectors.toList());
    }

    private List<String> createPaginatedQueries(String query, int pages) {
        return IntStream.rangeClosed(0, pages)
                .mapToObj(index -> buildAccessionQueryUrl(ebeyeIndexUrl.getDefaultSearchIndexUrl(),
                        query,
                        DEFAULT_EBI_SEARCH_LIMIT,
                        index * DEFAULT_EBI_SEARCH_LIMIT))
                .collect(Collectors.toList());
    }

    private List<String> extractAccessionsFromFuture(ListenableFuture<ResponseEntity<EbeyeSearchResult>> future) {
        List<String> accessions;

        try {
            ResponseEntity<EbeyeSearchResult> response = future.get();
            EbeyeSearchResult searchResult = response.getBody();

            accessions = searchResult.getEntries()
                    .stream()
                    .map(Entry::getUniprotAccession)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Unable to read response from future:", e);
            accessions = new ArrayList<>();
        }

        return accessions;
    }

    private ListenableFuture<ResponseEntity<EbeyeSearchResult>> getEbeyeSearchFutureResponse(String queryUrl) {
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