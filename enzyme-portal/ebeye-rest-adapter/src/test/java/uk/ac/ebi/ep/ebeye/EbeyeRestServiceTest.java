package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 * Tests the behaviour of the {@link uk.ac.ebi.ep.ebeye.EbeyeRestService} class.
 */
public class EbeyeRestServiceTest {

    private static final int MAX_ENTRIES_IN_RESPONSE = 10;
    private static final int CHUNK_SIZE = 10;

    private static final String SERVER_URL = "http://www.myserver.com/ebeye";
    private static final String AUTOCOMPLETE_REQUEST = SERVER_URL + "/autocomplete?term=%s&format=json";
    private static final String ACCESSION_REQUEST = SERVER_URL + "?query=%s&size=%d&start=%d&fields=name,status&format=json";

    private EbeyeRestService restService;

    private MockRestServiceServer syncRestServerMock;
    private MockRestServiceServer asyncRestServerMock;
    private final int requestTimeout = 5000;

    public HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory() {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(500)
                .build();

        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
        factory.setReadTimeout(requestTimeout);
        factory.setConnectTimeout(requestTimeout);
        factory.setHttpAsyncClient(httpclient);
        return factory;

    }

    @Before
    public void setUp() {
        EbeyeIndexUrl serverUrl = new EbeyeIndexUrl();
        serverUrl.setDefaultSearchIndexUrl(SERVER_URL);
        serverUrl.setChunkSize(CHUNK_SIZE);
        serverUrl.setMaxEbiSearchLimit(MAX_ENTRIES_IN_RESPONSE);

        RestTemplate restTemplate = new RestTemplate();
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(httpComponentsAsyncClientHttpRequestFactory());

        syncRestServerMock = MockRestServiceServer.createServer(restTemplate);
        asyncRestServerMock = MockRestServiceServer.createServer(asyncRestTemplate);

        restService = new EbeyeRestService(serverUrl, restTemplate, asyncRestTemplate);
    }

    @After
    public void tearDown() throws Exception {
        httpComponentsAsyncClientHttpRequestFactory().destroy();
    }

    @Test
    public void non_matching_term_ppp_sent_to_autocomplete_returns_no_suggestions() throws Exception {
        String searchTerm = "ppp";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions = createSuggestions();
        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        syncRestServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = restService.autocompleteSearch(searchTerm);

        syncRestServerMock.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    @Test
    public void partial_term_phos_sent_to_autocomplete_returns_valid_suggestions() throws Exception {
        String searchTerm = "phos";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions
                = createSuggestions("phosphate", "phosphoenolpyruvate", "phosphohydrolase");
        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        syncRestServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = restService.autocompleteSearch(searchTerm);

        syncRestServerMock.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    @Test
    public void null_query_in_unique_accession_search_throws_exception() throws Exception {
        String query = null;
        int limit = 1;

        try {
            restService.queryForUniqueAccessions(query, limit);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), is("Query can not be null"));
        }
    }

    @Test
    public void negative_limit_in_unique_accession_search_throws_exception() throws Exception {
        String query = "query";
        int limit = -1;

        try {
            restService.queryForUniqueAccessions(query, limit);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), is("Limit can not be less than 1"));
        }
    }

    @Test
    public void zero_limit_in_accession_search_throws_exception() throws Exception {
        String query = "query";
        int limit = 0;

        try {
            restService.queryForUniqueAccessions(query, limit);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), is("Limit can not be less than 1"));
        }
    }

    @Test
    public void
            query_response_in_unique_accession_search_has_hitCount_less_than_maxEntries_only_synchronous_request_used()
            throws Exception {
        String query = "query";
        int limit = MAX_ENTRIES_IN_RESPONSE;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult searchResult = createAccessionResult(createEntries(limit));
        mockServerResponse(syncRestServerMock, requestUrl, searchResult);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(limit));
    }

    @Test
    public void query_response_in_unique_accessions_has_hitCount_greater_than_maxEntries_both_requests_used()
            throws Exception {
        String query = "query";
        int limit = MAX_ENTRIES_IN_RESPONSE + 1;

        List<Entry> entries = createEntries(limit);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createAccessionResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), limit);

        mockServerResponse(syncRestServerMock, queryChunkUrl1, chunkedResult1);
        mockServerResponse(asyncRestServerMock, queryChunkUrl1, chunkedResult1);

        EbeyeSearchResult resultSet2 = createAccessionResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, limit), limit);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockServerResponse(asyncRestServerMock, queryChunkRequestUrl2, resultSet2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(limit));
    }

    @Test
    public void query_limited_to_1_entry_in_unique_accession_search_returns_1_entry() throws Exception {
        String query = "query";
        int limit = 1;

        String requestUrl = String.format(ACCESSION_REQUEST, query, MAX_ENTRIES_IN_RESPONSE, 0);
        List<Entry> entries = createEntries(MAX_ENTRIES_IN_RESPONSE);
        EbeyeSearchResult searchResult = createAccessionResult(entries);

        mockServerResponse(syncRestServerMock, requestUrl, searchResult);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(limit));
        assertThat(actualAccessions.get(0), is(entries.get(0).getUniprotAccession()));
    }

    @Test
    public void query_with_no_limits_in_unique_accession_search_returns_all_entries() throws Exception {
        String query = "query";
        int entryNum = MAX_ENTRIES_IN_RESPONSE * 2;

        List<Entry> entries = createEntries(entryNum);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1
                = createAccessionResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entryNum);

        mockServerResponse(syncRestServerMock, queryChunkUrl1, chunkedResult1);
        mockServerResponse(asyncRestServerMock, queryChunkUrl1, chunkedResult1);

        EbeyeSearchResult resultSet2
                = createAccessionResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entryNum), entryNum);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockServerResponse(asyncRestServerMock, queryChunkRequestUrl2, resultSet2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(entryNum));
    }

    @Test
    public void exception_is_thrown_on_synchronous_request_for_unique_accessions_search_returns_empty_accession_list()
            throws Exception {
        String query = "query";

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        syncRestServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(0));
    }

    @Test
    public void exception_is_thrown_on_first_asynchronous_chunk_to_unique_accession_search_returns_remaining_chunks()
            throws Exception {
        String query = "query";
        int expectedEntryNum = MAX_ENTRIES_IN_RESPONSE;
        int entryNum = expectedEntryNum * 2;

        List<Entry> entries = createEntries(entryNum);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1
                = createAccessionResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entryNum);

        mockServerResponse(syncRestServerMock, queryChunkUrl1, chunkedResult1);

        asyncRestServerMock.expect(requestTo(queryChunkUrl1)).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        EbeyeSearchResult resultSet2
                = createAccessionResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entryNum), entryNum);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockServerResponse(asyncRestServerMock, queryChunkRequestUrl2, resultSet2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(expectedEntryNum));
    }

    @Test
    public void duplicate_entry_in_synchronous_request_in_unique_accession_search_is_removed_in_response()
            throws Exception {
        String query = "query";

        Entry entry = createEntryWithIdAndDefaultNameAndTitle("entry1");
        Entry duplicateEntry = createEntryWithIdAndDefaultNameAndTitle("entry1");

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult searchResult = createAccessionResult(Arrays.asList(entry, duplicateEntry));

        mockServerResponse(syncRestServerMock, requestUrl, searchResult);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        syncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(1));
    }

    @Test
    public void duplicate_entries_in_asynchronous_request_in_unique_accession_search_are_removed_in_response()
            throws Exception {
        String query = "query";
        int uniqueEntryNum = MAX_ENTRIES_IN_RESPONSE;
        int totalEntryNum = uniqueEntryNum * 2;

        List<Entry> entries = createEntries(uniqueEntryNum);

        String queryChunkRequestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createAccessionResult(entries, totalEntryNum);

        mockServerResponse(syncRestServerMock, queryChunkRequestUrl1, chunkedResult1);
        mockServerResponse(asyncRestServerMock, queryChunkRequestUrl1, chunkedResult1);

        List<Entry> duplicateEntries = new ArrayList<>(entries);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, uniqueEntryNum);
        EbeyeSearchResult chunkedResult2 = createAccessionResult(duplicateEntries, totalEntryNum);

        mockServerResponse(asyncRestServerMock, queryChunkRequestUrl2, chunkedResult2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(uniqueEntryNum));
    }

    @Test
    public void maxRetrievableEntries_is_15_and_number_of_hits_is_20_unique_accession_search_returns_only_15()
            throws Exception {
        int maxRetrievableEntries = 10;
        restService.setMaxRetrievableHits(maxRetrievableEntries);

        String query = "query";

        int entryNum = 15;

        List<Entry> entries = createEntries(entryNum);

        String queryChunkRequestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createAccessionResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entryNum);

        mockServerResponse(syncRestServerMock, queryChunkRequestUrl1, chunkedResult1);
        mockServerResponse(asyncRestServerMock, queryChunkRequestUrl1, chunkedResult1);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        syncRestServerMock.verify();
        asyncRestServerMock.verify();

        assertThat(actualAccessions, hasSize(maxRetrievableEntries));
    }

    private EbeyeAutocomplete createAutoCompleteResponse(List<Suggestion> suggestedKeywords) {
        EbeyeAutocomplete autocompleteResponse = new EbeyeAutocomplete();
        autocompleteResponse.getSuggestions().addAll(suggestedKeywords);

        return autocompleteResponse;
    }

    private List<Suggestion> createSuggestions(String... suggestedKeyword) {
        return Arrays.stream(suggestedKeyword)
                .map(Suggestion::new)
                .collect(Collectors.toList());
    }

    private String convertToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private EbeyeSearchResult createAccessionResult(List<Entry> entries) {
        EbeyeSearchResult result = new EbeyeSearchResult();
        result.setEntries(entries);
        result.setHitCount(entries.size());

        return result;
    }

    private EbeyeSearchResult createAccessionResult(List<Entry> entries, int hitCount) {
        EbeyeSearchResult result = new EbeyeSearchResult();
        result.setEntries(entries);
        result.setHitCount(hitCount);

        return result;
    }

    private List<Entry> createEntries(int num) {
        return IntStream.range(0, num)
                .mapToObj(index -> createEntryWithIdAndDefaultNameAndTitle("entry" + index))
                .collect(Collectors.toList());
    }

    private Entry createEntryWithIdAndDefaultNameAndTitle(String id) {
        Entry entry = new Entry(id, id + "_" + id);
        entry.setTitle("title_" + id);
        return entry;
    }

    private void mockServerResponse(MockRestServiceServer serverMock, String requestUrl, EbeyeSearchResult searchResult)
            throws IOException {
        serverMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(searchResult), MediaType.APPLICATION_JSON));
    }

    private String createQueryUrl(String query, int size, int start) {
        return String.format(ACCESSION_REQUEST, query, MAX_ENTRIES_IN_RESPONSE, start);
    }
}
