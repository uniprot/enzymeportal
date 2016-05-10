package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Parent class for the {@link EbeyeRestServiceTest} and the {@link EnzymeCentricServiceTest} classes.
 *
 * This class is responsible for setting up common configurations, and holding utility methods shared between the
 * test classes.
 */
public abstract class XCentricSetup {
    protected static final String SERVER_URL = "http://www.myserver.com/ebeye";
    protected static final int MAX_ENTRIES_IN_RESPONSE = 10;
    protected static final int CHUNK_SIZE = 10;
    private static final String EBEYE_REQUEST = SERVER_URL + "?query=%s&size=%d&start=%d&fields=name&format=json";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected MockRestServiceServer restServerMock;
    protected EbeyeIndexUrl serverUrl;
    protected RestTemplate restTemplate;

    protected void setUp() throws Exception {
        serverUrl = new EbeyeIndexUrl();
        serverUrl.setChunkSize(CHUNK_SIZE);
        serverUrl.setMaxEbiSearchLimit(MAX_ENTRIES_IN_RESPONSE);
        setEbeyeSearchURL();

        restTemplate = new RestTemplate();

        restServerMock = MockRestServiceServer.createServer(restTemplate);
    }

    protected abstract void setEbeyeSearchURL();

    protected String convertToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    protected EbeyeSearchResult createSearchResult(List<Entry> entries) {
        EbeyeSearchResult result = new EbeyeSearchResult();
        result.setEntries(entries);
        result.setHitCount(entries.size());

        return result;
    }

    protected EbeyeSearchResult createSearchResult(List<Entry> entries, int hitCount) {
        EbeyeSearchResult result = new EbeyeSearchResult();
        result.setEntries(entries);
        result.setHitCount(hitCount);

        return result;
    }

    protected String createQueryUrl(String query, int size, int start) {
        return String.format(EBEYE_REQUEST, query, MAX_ENTRIES_IN_RESPONSE, start);
    }

    protected void mockSuccessfulServerResponse(MockRestServiceServer serverMock, String requestUrl,
            EbeyeSearchResult searchResult)
            throws IOException {
        serverMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(searchResult), MediaType.APPLICATION_JSON));
    }

    protected void mockInternalServerErrorResponse(MockRestServiceServer serverMock, String requestUrl)
            throws IOException {
        serverMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
    }

    protected List<Entry> createEntries(int num, Function<String, Entry> entryCreator) {
        return IntStream.range(0, num)
                .mapToObj(String::valueOf)
                .map(entryCreator::apply)
                .collect(Collectors.toList());
    }
}
