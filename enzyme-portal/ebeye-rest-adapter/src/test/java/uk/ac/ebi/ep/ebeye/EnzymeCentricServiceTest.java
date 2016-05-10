package uk.ac.ebi.ep.ebeye;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestTemplate;

import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricServiceTest extends XCentricSetup {
    private Function<String, Entry> entryCreator;

    private EnzymeCentricService enzymeCentricService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        entryCreator = createEntry();

        enzymeCentricService = new EnzymeCentricService(serverUrl, restTemplate);
    }

    @Override protected void setEbeyeSearchURL() {
        serverUrl.setEnzymeCentricSearchUrl(SERVER_URL);
    }

    @Test
    public void null_rest_template_in_constructor_throws_exception() throws Exception {
        EbeyeIndexUrl serverUrl = new EbeyeIndexUrl();
        RestTemplate restTemplate = null;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'restTemplate' must not be null");

        enzymeCentricService = new EnzymeCentricService(serverUrl, restTemplate);
    }

    @Test
    public void null_service_configuration_object_in_constructor_throws_exception() throws Exception {
        EbeyeIndexUrl serverUrl = null;
        RestTemplate restTemplate = new RestTemplate();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'ebeyeIndexUrl' must not be null");

        enzymeCentricService = new EnzymeCentricService(serverUrl, restTemplate);
    }

    @Test
    public void null_EcQuery_in_query_for_EcNumbers_throws_exception() throws Exception {
        String query = null;
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query cannot be null");

        enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
    }

    @Test
    public void negative_retrieval_limit_value_in_query_for_EcNumbers_throws_exception() throws Exception {
        String query = "kinase";
        int limit = -1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit must be greater than 0");

        enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
    }

    @Test
    public void zero_retrieval_limit_value_in_query_for_EcNumbers_throws_exception() throws Exception {
        String query = "kinase";
        int limit = 0;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit must be greater than 0");

        enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
    }

    @Test
    public void query_generates_zero_hitCount_so_service_submits_only_one_request_to_ebeyeSearch() throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        List<Entry> entries = createEntries(0, entryCreator);
        EbeyeSearchResult result = createSearchResult(entries);

        restServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(result), MediaType.APPLICATION_JSON));

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(0));
    }

    @Test
    public void
    query_generates_hitCount_less_than_MAX_ENTRIES_IN_RESPONSE_so_service_submits_only_one_request_to_ebeyeSearch()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;
        int entriesSize = MAX_ENTRIES_IN_RESPONSE - 1;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        List<Entry> entries = createEntries(entriesSize, entryCreator);
        EbeyeSearchResult result = createSearchResult(entries);

        mockSuccessfulServerResponse(restServerMock, requestUrl, result);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(lessThan(MAX_ENTRIES_IN_RESPONSE)));
    }

    @Test
    public void
    query_generates_hitCount_greater_than_MAX_ENTRIES_IN_RESPONSE_so_service_submits_multiple_requests_to_ebeyeSearch()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;

        int entriesSize = MAX_ENTRIES_IN_RESPONSE + 1;

        String requestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        List<Entry> entries = createEntries(entriesSize, entryCreator);
        EbeyeSearchResult result1 =
                createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entries.size());

        mockSuccessfulServerResponse(restServerMock, requestUrl1, result1);

        String requestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        EbeyeSearchResult result2 = createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entries.size()));

        mockSuccessfulServerResponse(restServerMock, requestUrl2, result2);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(entriesSize));
    }

    @Test
    public void
    query_generates_3_entries_with_same_ecNumber_service_filters_out_entries_with_same_ecNumber_and_returns_only_one_entry()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        String ecNumber = "1.2.3.4";

        Entry entry1 = createEntry().apply(ecNumber);
        Entry entry2 = createEntry().apply(ecNumber);
        Entry entry3 = createEntry().apply(ecNumber);

        List<Entry> sameEntries = Arrays.asList(entry1, entry2, entry3);

        EbeyeSearchResult result = createSearchResult(sameEntries);

        mockSuccessfulServerResponse(restServerMock, requestUrl, result);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(1));
    }

    @Test
    public void query_generates_20_entries_but_limit_cap_of_15_reduces_response_to_15_entries()
            throws Exception {
        String query = "query";
        int limit = 15;
        int entriesSize = 20;

        List<Entry> entries = createEntries(entriesSize, entryCreator);

        String requestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult result1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl1, result1);

        String requestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);
        EbeyeSearchResult result2 =
                createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entries.size()), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl2, result2);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(limit));
    }

    @Test
    public void query_generates_20_entries_and_limit_is_capped_to_MAX_VALUE_so_all_entries_returned() throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;
        int entriesSize = 20;

        List<Entry> entries = createEntries(entriesSize, entryCreator);

        String requestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult result1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl1, result1);

        String requestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);
        EbeyeSearchResult result2 =
                createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entries.size()), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl2, result2);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(entriesSize));
    }

    @Test
    public void ebeye_server_fails_to_reply_to_first_query_on_first_attempt_but_succeeds_on_second()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;
        int entriesSize = MAX_ENTRIES_IN_RESPONSE;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        mockInternalServerErrorResponse(restServerMock, requestUrl);

        List<Entry> entries = createEntries(entriesSize, entryCreator);
        EbeyeSearchResult searchResult = createSearchResult(entries);

        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(entriesSize));
    }

    @Test
    public void
    ebeye_server_fails_to_reply_to_first_query_on_first_and_retry_attempts_so_service_returns_an_empty_list_of_ec_numbers()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        mockInternalServerErrorResponse(restServerMock, requestUrl);
        mockInternalServerErrorResponse(restServerMock, requestUrl);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(0));
    }

    @Test
    public void
    ebeye_server_fails_to_reply_to_second_query_on_first_attempt_but_succeeds_on_second_so_service_returns_only_first_set_of_ec_numbers()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;
        int entriesSize = 20;

        List<Entry> entries = createEntries(entriesSize, entryCreator);

        String requestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult result1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl1, result1);

        String requestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        restServerMock.expect(requestTo(requestUrl2)).andRespond(withServerError());

        EbeyeSearchResult result2 =
                createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entriesSize), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl2, result2);

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(entriesSize));
    }

    @Test
    public void
    ebeye_server_fails_to_reply_to_second_query_on_first_and_retry_attempt_so_service_returns_only_first_set_of_ec_numbers()
            throws Exception {
        String query = "query";
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;
        int entriesSize = 20;

        List<Entry> entries = createEntries(entriesSize, entryCreator);

        String requestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult result1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl1, result1);

        String requestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        restServerMock.expect(requestTo(requestUrl2)).andRespond(withServerError());
        restServerMock.expect(requestTo(requestUrl2)).andRespond(withServerError());

        List<String> ecNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        restServerMock.verify();

        assertThat(ecNumbers, hasSize(10));
    }

    private Function<String, Entry> createEntry() {
        return id -> {
            Entry entry = new Entry(id, id + "_" + id);
            entry.setEc("ec_" + id);
            return entry;
        };
    }
}