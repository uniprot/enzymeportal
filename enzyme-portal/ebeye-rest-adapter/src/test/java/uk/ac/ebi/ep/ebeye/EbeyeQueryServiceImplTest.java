package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests the behaviour of the {@link uk.ac.ebi.ep.ebeye.EbeyeQueryServiceImpl} class.
 */
public class EbeyeQueryServiceImplTest {
    private static final String SERVER_URL = "http://www.myserver.com/ebeye";
    private static final int MAX_ENTRIES_IN_RESPONSE = 10;
    private static final int CHUNK_SIZE = 10;
    private static final String EBEYE_REQUEST = SERVER_URL + "?query=%s&size=%d&start=%d&fields=name&format=json";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private EbeyeQueryServiceImpl ebeyeQueryService;

    private MockRestServiceServer restServerMock;
    private Function<String, Entry> entryCreator;

    @Before
    public void setUp() throws Exception {
        EbeyeIndexProps serverUrl = new EbeyeIndexProps();
        serverUrl.setChunkSize(CHUNK_SIZE);
        serverUrl.setMaxEbiSearchLimit(MAX_ENTRIES_IN_RESPONSE);
        serverUrl.setEbeyeSearchUrl(SERVER_URL);

        RestTemplate restTemplate = new RestTemplate();

        restServerMock = MockRestServiceServer.createServer(restTemplate);

        ebeyeQueryService = new EbeyeQueryServiceImpl(serverUrl, restTemplate);

        entryCreator = createEntry();
    }

    @Test
    public void null_rest_template_in_constructor_throws_exception() throws Exception {
        EbeyeIndexProps serverUrl = new EbeyeIndexProps();
        RestTemplate restTemplate = null;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'restTemplate' must not be null");

        ebeyeQueryService = new EbeyeQueryServiceImpl(serverUrl, restTemplate);
    }

    @Test
    public void null_service_configuration_object_in_constructor_throws_exception() throws Exception {
        EbeyeIndexProps serverUrl = null;
        RestTemplate restTemplate = new RestTemplate();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'ebeyeIndexProps' must not be null");

        ebeyeQueryService = new EbeyeQueryServiceImpl(serverUrl, restTemplate);
    }

    @Test
    public void null_query_throws_exception() throws Exception {
        String query = null;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query can not be null");

        ebeyeQueryService.executeQuery(query);
    }

    @Test
    public void execute_query_returns_zero_entries_because_ebeye_server_finds_zero_entries() throws Exception {
        String query = "query";

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult searchResult = createSearchResult(Collections.emptyList());
        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);
        TestSubscriber subscriber = subscribe(entryObs);

        subscriber.assertCompleted();
        subscriber.assertNoValues();

        restServerMock.verify();
    }

    @Test
    public void ebeye_server_returns_hitCount_less_than_maxEntries_so_only_single_request_used() throws Exception {
        String query = "query";
        int limit = MAX_ENTRIES_IN_RESPONSE;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult searchResult = createSearchResult(createEntries(limit, entryCreator));
        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);
        TestSubscriber subscriber = subscribe(entryObs);

        subscriber.assertValueCount(limit);

        restServerMock.verify();
    }

    @Test
    public void ebeye_server_returns_hitCount_greater_than_maxEntries_so_multi_requests_used() throws Exception {
        String query = "query";
        int limit = MAX_ENTRIES_IN_RESPONSE + 1;

        List<Entry> entries = createEntries(limit, entryCreator);

        String queryChunkUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult resultSet1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE), limit);

        mockSuccessfulServerResponse(restServerMock, queryChunkUrl1, resultSet1);

        EbeyeSearchResult resultSet2 = createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, limit), limit);
        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl2, resultSet2);

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);

        TestSubscriber<Entry> testSubscriber = subscribe(entryObs);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
        testSubscriber.assertReceivedOnNext(entries);

        restServerMock.verify();
    }

    @Test
    public void ebeye_server_fails_to_reply_to_first_query_during_first_attempt_but_succeeds_on_second()
            throws Exception {
        String query = "query";
        int entriesSize = MAX_ENTRIES_IN_RESPONSE;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        mockInternalServerErrorResponse(restServerMock, requestUrl);

        List<Entry> entries = createEntries(entriesSize, entryCreator);
        EbeyeSearchResult searchResult = createSearchResult(entries);

        mockSuccessfulServerResponse(restServerMock, requestUrl, searchResult);

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);

        TestSubscriber<Entry> subscriber = subscribe(entryObs);
        subscriber.assertCompleted();
        subscriber.assertReceivedOnNext(entries);

        restServerMock.verify();
    }

    @Test
    public void
    ebeye_server_fails_to_reply_to_first_query_during_first_and_retry_attempts_so_service_returns_an_empty_observable()
            throws Exception {
        String query = "query";

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        mockInternalServerErrorResponse(restServerMock, requestUrl);
        mockInternalServerErrorResponse(restServerMock, requestUrl);

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);

        TestSubscriber<Entry> subscriber = subscribe(entryObs);
        subscriber.assertCompleted();
        subscriber.assertNoValues();

        restServerMock.verify();
    }

    @Test
    public void
    ebeye_server_fails_to_reply_to_second_query_on_first_attempt_but_succeeds_on_second_so_service_returns_all_entries()
            throws Exception {
        String query = "query";
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

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);

        TestSubscriber<Entry> subscriber = subscribe(entryObs);
        subscriber.awaitTerminalEvent();
        subscriber.assertCompleted();
        subscriber.assertReceivedOnNext(entries);

        restServerMock.verify();
    }

    @Test
    public void
    ebeye_server_fails_to_reply_to_second_query_on_first_and_retry_attempt_so_service_returns_only_first_set_of_entries()
            throws Exception {
        String query = "query";
        int entriesSize = 20;

        List<Entry> entries = createEntries(entriesSize, entryCreator);

        String requestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        List<Entry> firstEntryList = entries.subList(0, MAX_ENTRIES_IN_RESPONSE);
        EbeyeSearchResult result1 = createSearchResult(firstEntryList, entriesSize);

        mockSuccessfulServerResponse(restServerMock, requestUrl1, result1);

        String requestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);

        restServerMock.expect(requestTo(requestUrl2)).andRespond(withServerError());
        restServerMock.expect(requestTo(requestUrl2)).andRespond(withServerError());

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);

        TestSubscriber<Entry> subscriber = subscribe(entryObs);
        subscriber.awaitTerminalEvent();
        subscriber.assertCompleted();
        subscriber.assertReceivedOnNext(firstEntryList);

        restServerMock.verify();
    }

    @Test
    public void maxRetrievableEntries_is_15_and_number_of_hits_is_20_unique_accession_search_returns_only_15()
            throws Exception {
        int maxRetrievableEntries = 15;
        ebeyeQueryService.setMaxRetrievableHits(maxRetrievableEntries);

        String query = "query";

        int entryNum = 20;

        List<Entry> entries = createEntries(entryNum, entryCreator);

        String queryChunkRequestUrl1 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);
        EbeyeSearchResult chunkedResult1 = createSearchResult(entries.subList(0, MAX_ENTRIES_IN_RESPONSE),
                entryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl1, chunkedResult1);

        String queryChunkRequestUrl2 = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, MAX_ENTRIES_IN_RESPONSE);
        EbeyeSearchResult chunkedResult2 =
                createSearchResult(entries.subList(MAX_ENTRIES_IN_RESPONSE, entryNum), entryNum);

        mockSuccessfulServerResponse(restServerMock, queryChunkRequestUrl2, chunkedResult2);

        Observable<Entry> entryObs = ebeyeQueryService.executeQuery(query);

        TestSubscriber<Entry> subscriber = subscribe(entryObs);
        subscriber.awaitTerminalEvent();
        subscriber.assertCompleted();
        subscriber.assertValueCount(maxRetrievableEntries);

        restServerMock.verify();
    }

    private String createQueryUrl(String query, int size, int start) {
        return String.format(EBEYE_REQUEST, query, MAX_ENTRIES_IN_RESPONSE, start);
    }

    private EbeyeSearchResult createSearchResult(List<Entry> entries, int hitCount) {
        EbeyeSearchResult result = new EbeyeSearchResult();
        result.setEntries(entries);
        result.setHitCount(hitCount);

        return result;
    }

    private EbeyeSearchResult createSearchResult(List<Entry> entries) {
        return createSearchResult(entries, entries.size());
    }

    private void mockSuccessfulServerResponse(MockRestServiceServer serverMock, String requestUrl,
            EbeyeSearchResult searchResult)
            throws IOException {
        serverMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(searchResult), MediaType.APPLICATION_JSON));
    }

    private void mockInternalServerErrorResponse(MockRestServiceServer serverMock, String requestUrl)
            throws IOException {
        serverMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
    }

    private String convertToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private List<Entry> createEntries(int num, Function<String, Entry> entryCreator) {
        return IntStream.range(0, num)
                .mapToObj(String::valueOf)
                .map(entryCreator::apply)
                .collect(Collectors.toList());
    }

    private Function<String, Entry> createEntry() {
        return id -> new Entry(id, id + "_" + id);
    }

    private <T> TestSubscriber<T> subscribe(Observable<T> observable) {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);

        return testSubscriber;
    }
}