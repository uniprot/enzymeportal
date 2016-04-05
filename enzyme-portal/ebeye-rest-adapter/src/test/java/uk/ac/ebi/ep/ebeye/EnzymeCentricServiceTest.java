/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricServiceTest {

    private static final int MAX_ENTRIES_IN_RESPONSE = 4;
    private static final String SERVER_URL = "http://www.myserver.com/ebeye";
    private static final String AUTOCOMPLETE_REQUEST = SERVER_URL + "/autocomplete?term=%s&format=json";
    private static final String EC_REQUEST = SERVER_URL + "?query=%s&size=%d&start=%d&fields=name&format=json";

    private MockRestServiceServer syncRestServerMock;
    private MockRestServiceServer asyncRestServerMock;

    private EnzymeCentricService enzymeCentricService;

    @Before
    public void setUp() {
        EbeyeIndexUrl serverUrl = new EbeyeIndexUrl();
        serverUrl.setEnzymeCentricSearchUrl(SERVER_URL);
        serverUrl.setMaxEbiSearchLimit(MAX_ENTRIES_IN_RESPONSE);

        RestTemplate restTemplate = new RestTemplate();
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        syncRestServerMock = MockRestServiceServer.createServer(restTemplate);
        asyncRestServerMock = MockRestServiceServer.createServer(asyncRestTemplate);

        enzymeCentricService = new EnzymeCentricService(serverUrl, restTemplate, asyncRestTemplate);

    }

    @Test
    public void
            query_response_in_unique_accession_search_has_hitCount_less_than_maxEntries_only_synchronous_request_used()
            throws Exception {
        String query = "query";
        String filename = "enzyme.json";
        int limit = MAX_ENTRIES_IN_RESPONSE;

        String requestUrl = createQueryUrl(query, MAX_ENTRIES_IN_RESPONSE, 0);

        mockServerResponse(syncRestServerMock, requestUrl, limit, filename);

        List<String> actualEcNumbers = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        syncRestServerMock.verify();

        assertThat(actualEcNumbers, hasSize(limit));
    }

    private void mockServerResponse(MockRestServiceServer serverMock, String requestUrl, int limit, String filename)
            throws IOException {
        //String filename = "enzyme.json";
        String json = getJsonFile(filename);
        for (int i = 1; i < limit - 1; i++) {
            serverMock.expect(requestTo(requestUrl))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
        }
    }

    private String createQueryUrl(String query, int size, int start) {
        return String.format(EC_REQUEST, query, MAX_ENTRIES_IN_RESPONSE, start);
    }

    protected String getJsonFile(String filename) {
        try {
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream(filename);

            return IOUtils.toString(in);
        } catch (IOException ex) {
            //LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

}
