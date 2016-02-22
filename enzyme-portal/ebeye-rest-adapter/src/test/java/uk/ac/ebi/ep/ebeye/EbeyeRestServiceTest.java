package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests the behaviour of the {@link uk.ac.ebi.ep.ebeye.EbeyeRestService} class.
 */
public class EbeyeRestServiceTest {
    private static final String SERVER_URL = "http://www.myserver.com/ebeye";
    private static final String AUTOCOMPLETE_REQUEST = SERVER_URL + "/autocomplete?term=%s&format=json";

    private EbeyeRestService restService;

    private MockRestServiceServer mockRestServer;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        mockRestServer = MockRestServiceServer.createServer(restTemplate);

        EbeyeIndexUrl serverUrl = new EbeyeIndexUrl();
        serverUrl.setDefaultSearchIndexUrl(SERVER_URL);

        restService = new EbeyeRestService(serverUrl, restTemplate, asyncRestTemplate);
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Test
    public void non_matching_term_ppp_sent_to_Ebeye_autocomplete_returns_no_suggestions() throws Exception {
        String searchTerm = "ppp";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions = createSuggestions();

        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        mockRestServer.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = restService.ebeyeAutocompleteSearch(searchTerm);

        mockRestServer.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
    }

    @Test
    public void partial_term_phos_sent_to_Ebeye_autocomplete_returns_valid_suggestions() throws Exception {
        String searchTerm = "phos";

        String requestUrl = String.format(AUTOCOMPLETE_REQUEST, searchTerm);

        List<Suggestion> expectedSuggestions =
                createSuggestions("phosphate", "phosphoenolpyruvate", "phosphohydrolase");

        EbeyeAutocomplete autocompleteResponse = createAutoCompleteResponse(expectedSuggestions);

        mockRestServer.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(autocompleteResponse), MediaType.APPLICATION_JSON));

        List<Suggestion> actualSuggestions = restService.ebeyeAutocompleteSearch(searchTerm);

        mockRestServer.verify();

        assertThat(actualSuggestions, is(expectedSuggestions));
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
}
