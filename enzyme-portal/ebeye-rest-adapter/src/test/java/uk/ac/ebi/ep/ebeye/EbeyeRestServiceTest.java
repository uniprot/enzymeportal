package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 * Tests the behaviour of the {@link uk.ac.ebi.ep.ebeye.EbeyeRestService} class.
 */
public class EbeyeRestServiceTest extends XCentricSetup {
    private static final String AUTOCOMPLETE_REQUEST = SERVER_URL + "/autocomplete?term=%s&format=json";

    private Function<String, Entry> entryCreator;
    private EbeyeRestService restService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        entryCreator = createEntry();

        restService = new EbeyeRestService(serverUrl, restTemplate);
    }

    @Override protected void setEbeyeSearchURL() {
        serverUrl.setDefaultSearchIndexUrl(SERVER_URL);
    }

    @Test
    public void non_matching_term_ppp_sent_to_autocomplete_returns_no_suggestions() throws Exception {
        String searchTerm = "ppp";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions = createSuggestions();
        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        restServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = restService.autocompleteSearch(searchTerm);

        restServerMock.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    @Test
    public void partial_term_phos_sent_to_autocomplete_returns_valid_suggestions() throws Exception {
        String searchTerm = "phos";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions =
                createSuggestions("phosphate", "phosphoenolpyruvate", "phosphohydrolase");
        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        restServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = restService.autocompleteSearch(searchTerm);

        restServerMock.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    @Test
    public void null_query_in_unique_accession_search_throws_exception() throws Exception {
        String query = null;
        int limit = 1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query can not be null");

        restService.queryForUniqueAccessions(query, limit);
    }

    @Test
    public void negative_limit_in_unique_accession_search_throws_exception() throws Exception {
        String query = "query";
        int limit = -1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit can not be less than 1");

        restService.queryForUniqueAccessions(query, limit);
    }

    @Test
    public void zero_limit_in_accession_search_throws_exception() throws Exception {
        String query = "query";
        int limit = 0;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit can not be less than 1");

        restService.queryForUniqueAccessions(query, limit);
    }

    @Test
    public void
    query_response_in_unique_accession_search_has_hitCount_less_than_maxEntries_only_single_request_used()
            throws Exception {
        String query = "query";
        int limit = MAX_ENTRIES_IN_RESPONSE;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult searchResult = createSearchResult(createEntries(limit, entryCreator));
        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        restServerMock.verify();

        assertThat(actualAccessions, hasSize(limit));
    }

    @Test
    public void
    query_response_in_unique_accessions_has_hitCount_greater_than_maxEntries_single_and_multi_requests_used()
            throws Exception {
        String query = "query";
        int limit = MAX_ENTRIES_IN_RESPONSE + 1;

        List<Entry> entries = createEntries(limit, entryCreator);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), limit);

        mockSuccessfulServerResponse(restServerMock, queryChunkUrl1, chunkedResult1);

        EbeyeSearchResult resultSet2 = createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, limit), limit);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl2, resultSet2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        assertThat(actualAccessions, hasSize(limit));
    }

    @Test
    public void query_limited_to_1_entry_in_unique_accession_search_returns_1_entry() throws Exception {
        String query = "query";
        int limit = 1;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        List<Entry> entries = createEntries(MAX_ENTRIES_IN_RESPONSE, entryCreator);
        EbeyeSearchResult searchResult = createSearchResult(entries);

        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        restServerMock.verify();

        assertThat(actualAccessions, hasSize(limit));
        assertThat(actualAccessions.get(0), is(entries.get(0).getUniprotAccession()));
    }

    @Test
    public void query_with_no_limits_in_unique_accession_search_returns_all_entries() throws Exception {
        String query = "query";
        int entryNum = MAX_ENTRIES_IN_RESPONSE * 2;

        List<Entry> entries = createEntries(entryNum, entryCreator);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 =
                createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkUrl1, chunkedResult1);

        EbeyeSearchResult resultSet2 =
                createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entryNum), entryNum);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl2, resultSet2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        assertThat(actualAccessions, hasSize(entryNum));
    }

    @Test
    public void exception_is_thrown_on_single_request_for_unique_accessions_search_returns_empty_accession_list()
            throws Exception {
        String query = "query";

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        mockInternalServerErrorResponse(restServerMock, requestUrl);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        restServerMock.verify();

        assertThat(actualAccessions, hasSize(0));
    }

    @Test
    public void
    exception_is_thrown_on_last_multi_threaded_request_to_unique_accession_search_returns_remaining_requests()
            throws Exception {
        String query = "query";
        int expectedEntryNum = MAX_ENTRIES_IN_RESPONSE;
        int entryNum = expectedEntryNum * 2;

        List<Entry> entries = createEntries(entryNum, entryCreator);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 =
                createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkUrl1, chunkedResult1);

        String queryChunkUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockInternalServerErrorResponse(restServerMock, queryChunkUrl2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);
        restServerMock.verify();

        assertThat(actualAccessions, hasSize(expectedEntryNum));
    }

    @Test
    public void duplicate_entry_in_single_request_in_unique_accession_search_is_removed_in_response()
            throws Exception {
        String query = "query";

        Entry entry = entryCreator.apply("1");
        Entry duplicateEntry = entryCreator.apply("1");

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult searchResult = createSearchResult(Arrays.asList(entry, duplicateEntry));

        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        restServerMock.verify();

        assertThat(actualAccessions, hasSize(1));
    }

    @Test
    public void duplicate_entries_in_multi_threaded_request_in_unique_accession_search_are_removed_in_response()
            throws Exception {
        String query = "query";
        int uniqueEntryNum = MAX_ENTRIES_IN_RESPONSE;
        int totalEntryNum = uniqueEntryNum * 2;

        List<Entry> entries = createEntries(uniqueEntryNum, entryCreator);

        String queryChunkRequestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createSearchResult(entries, totalEntryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl1, chunkedResult1);

        List<Entry> duplicateEntries = new ArrayList<>(entries);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, uniqueEntryNum);
        EbeyeSearchResult chunkedResult2 = createSearchResult(duplicateEntries, totalEntryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl2, chunkedResult2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

        assertThat(actualAccessions, hasSize(uniqueEntryNum));
    }

    @Test
    public void maxRetrievableEntries_is_15_and_number_of_hits_is_20_unique_accession_search_returns_only_15()
            throws Exception {
        int maxRetrievableEntries = 15;
        restService.setMaxRetrievableHits(maxRetrievableEntries);

        String query = "query";

        int entryNum = 20;

        List<Entry> entries = createEntries(entryNum, entryCreator);

        String queryChunkRequestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl1, chunkedResult1);

        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);
        EbeyeSearchResult chunkedResult2 =
                createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entryNum), entryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl2, chunkedResult2);

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, EbeyeRestService.NO_RESULT_LIMIT);

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

    private Function<String, Entry> createEntry() {
        return id -> {
            Entry entry = new Entry(id, id + "_" + id);
            entry.setTitle("title_" + id);
            return entry;
        };
    }
}