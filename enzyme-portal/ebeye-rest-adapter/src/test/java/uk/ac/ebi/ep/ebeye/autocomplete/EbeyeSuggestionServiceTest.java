
package uk.ac.ebi.ep.ebeye.autocomplete;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.EbeyeSuggestionService;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;

/**
 * Tests the behaviour of the {@link EbeyeSuggestionService} class.
 * @author joseph
 */
public class EbeyeSuggestionServiceTest {
    private static final String SERVER_URL = "http://www.myserver.com/ebeye";
    private static final int MAX_ENTRIES_IN_RESPONSE = 10;
    private static final int CHUNK_SIZE = 10;

    private static final String AUTOCOMPLETE_REQUEST = SERVER_URL + "/autocomplete?term=%s&format=json";

    private MockRestServiceServer restServerMock;

    private EbeyeSuggestionService ebeyeQueryService;

    @Before
    public void setUp()   {
        EbeyeIndexProps serverUrl = new EbeyeIndexProps();
        serverUrl.setChunkSize(CHUNK_SIZE);
        serverUrl.setMaxEbiSearchLimit(MAX_ENTRIES_IN_RESPONSE);
        serverUrl.setEnzymeCentricSearchUrl(SERVER_URL);

        RestTemplate restTemplate = new RestTemplate();

        restServerMock = MockRestServiceServer.createServer(restTemplate);

        ebeyeQueryService = new EbeyeSuggestionService(serverUrl, restTemplate);

    }

    @Test
    public void non_matching_term_ppp_sent_to_autocomplete_returns_no_suggestions() throws IOException  {
        String searchTerm = "ppp";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions = createSuggestions();
        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        restServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = ebeyeQueryService.autocompleteSearch(searchTerm);

        restServerMock.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    @Test
    public void partial_term_phos_sent_to_autocomplete_returns_valid_suggestions() throws IOException  {
        String searchTerm = "phos";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions =
                createSuggestions("phosphate", "phosphoenolpyruvate", "phosphohydrolase");
        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        restServerMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = ebeyeQueryService.autocompleteSearch(searchTerm);

        restServerMock.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    private String convertToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
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
}


