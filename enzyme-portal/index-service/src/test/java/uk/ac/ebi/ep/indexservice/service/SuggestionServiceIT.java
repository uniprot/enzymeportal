package uk.ac.ebi.ep.indexservice.service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.Assertions;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uk.ac.ebi.ep.indexservice.IndexServiceApplicationTests;
import uk.ac.ebi.ep.indexservice.config.IndexProperties;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Autocomplete;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Suggestion;

/**
 * This integration tests employs using several approaches; service bean,
 * webTestClient, webClient & okHttp client.
 *
 * @author Joseph
 */
@Slf4j
public class SuggestionServiceIT extends IndexServiceApplicationTests {

    private static final String AUTO_COMPLETE_URL = "/autocomplete?term={searchTerm}&format=json";
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private IndexProperties indexProperties;

    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("https://www.ebi.ac.uk/ebisearch/ws/rest")
            .build();

    private final WebClient webClient = WebClient.create();

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private Request request(String url) {

        return new Request.Builder()
                .url(url)
                .build();

    }

    public void testGetAutocomplete() {
        log.info("testAutocomplete");
        String searchTerm = "kinas";

        webTestClient
                .get()
                .uri(indexProperties.getEnzymeCentricUrl() + AUTO_COMPLETE_URL, searchTerm)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Autocomplete.class)
                .returnResult();

    }

    @Test
    public void testWebTestClientWithServerURL() {
        log.info("testWebTestClientWithServerURL");
        String searchTerm = "kinas";

        webTestClient.get()
                .uri(indexProperties.getEnzymeCentricUrl() + AUTO_COMPLETE_URL, searchTerm)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> Assertions.assertThat(response.getResponseBody())
                .isNotNull())
                .jsonPath("$.suggestions").isArray()
                .jsonPath("$.suggestions[0]").isNotEmpty()
                .jsonPath("$.suggestions[0].suggestion").isEqualTo("kinase");

    }

    @Test
    public void testWebTestClientForJsonResult() {
        log.info("testWebTestClientForJsonResult");
        String searchTerm = "kinas";

        webTestClient.get()
                .uri(indexProperties.getEnzymeCentricUrl() + AUTO_COMPLETE_URL, searchTerm)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.suggestions").isArray()
                .jsonPath("$.suggestions[0]").isNotEmpty()
                .jsonPath("$.suggestions[0].suggestion").value(containsString("kinase"));

    }

    @Test
    public void testGetAutocompleteResultWithWebTestClient() {
        log.info("testGetAutocompleteResultWithWebTestClient");
        String searchTerm = "canc";

        Autocomplete a = new Autocomplete();
        Suggestion s = new Suggestion();
        s.setSuggestedKeyword("cancellaria");
        a.getSuggestions().add(s);

        webTestClient.get()
                .uri(indexProperties.getEnzymeCentricUrl() + AUTO_COMPLETE_URL, searchTerm)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Autocomplete.class)
                .hasSize(1)
                .consumeWith(result -> {
                    assertNotNull(result);
                    assertNotNull(result.getResponseBody());
                });

    }

    @Test
    public void testShouldReceiveJsonAsString() {
        log.info("testShouldReceiveJsonAsString");
        String content = "{\"suggestions\":[{\"suggestion\":\"kinase\"},{\"suggestion\":\"kinase kinases\"}]}";

        String searchTerm = "kinas";

        Mono<String> result = this.webClient.get()
                .uri(indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl() + AUTO_COMPLETE_URL, searchTerm)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);

        StepVerifier.create(result)
                .expectNext(content)
                .expectComplete()
                .verify(Duration.ofSeconds(30));

        assertThat(result.block(), containsString(content));

    }

    /**
     * Test of findSuggestions method, of class SuggestionService.
     */
    @Test
    public void testFindSuggestions() {

        log.info("testFindSuggestions");
        String searchTerm = "liga";

        Suggestion s = new Suggestion();
        s.setSuggestedKeyword("ligase");

        List<Suggestion> result = suggestionService.findSuggestions(searchTerm);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result, hasItem(s));
        Assertions.assertThat(result).containsAnyOf(s);

    }

    /**
     * Test of autocomplete method, of class SuggestionService.
     */
    @Test
    public void testAutocomplete_with_service_method() {
        log.info("testAutocomplete_with_service_method");
        String searchTerm = "CANC";

        Mono<Autocomplete> result = suggestionService.autocomplete(searchTerm);

        result.subscribe(c -> {
            assertNotNull(c);
            assertThat(c.getSuggestions(), hasSize(greaterThanOrEqualTo(1)));
        });

    }

    @Test
    public void testAutocompleteSearch_with_service_method() {
        log.info("testAutocompleteSearch_with_service_method");
        String searchTerm = "homo sa";

        Mono<Autocomplete> result = suggestionService.autocompleteSearch(searchTerm);

        result.subscribe(c -> {
            assertNotNull(c);
            assertThat(c.getSuggestions(), hasSize(greaterThanOrEqualTo(1)));
        });

    }

    /**
     * Test of getSuggestions method, of class SuggestionService.
     */
    @Test
    public void testGetSuggestions_by_service_method() {
        log.info("testGetSuggestions_by_service_method");
        String searchTerm = "kina";

        Suggestion s = new Suggestion();
        s.setSuggestedKeyword("kinase");

        List<Suggestion> result = suggestionService.getSuggestions(searchTerm);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertTrue(result.contains(s));
        assertThat(result, hasItem(s));
        assertTrue(result.stream().anyMatch(suggestions -> "kinase".equals(suggestions.getSuggestedKeyword())));
        Assertions.assertThat(result).extracting(Suggestion::getSuggestedKeyword).containsAnyOf("kinase");
    }

    /**
     * Test of getSuggestions method, of class SuggestionService.
     */
    @Test
    public void testGetSuggestions_by_service_method_resource_id_search() {
        log.info("testGetSuggestions_by_service_method_resource_id_search");
        String id = "CHEBI:184";
        String searchTerm = "\"" + id.toLowerCase() + "\"";

        Suggestion s = new Suggestion();
        s.setSuggestedKeyword("chebi:18420");

        List<Suggestion> result = suggestionService.getSuggestions(searchTerm);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertTrue(result.contains(s));
        assertThat(result, hasItem(s));
        assertTrue(result.stream().anyMatch(suggestions -> "chebi:18420".equals(suggestions.getSuggestedKeyword())));
        Assertions.assertThat(result).extracting(Suggestion::getSuggestedKeyword).containsAnyOf("chebi:18420");
    }

    @Test
    public void testShouldReceiveJsonAsStringOkHttp() throws IOException {
        log.info("testShouldReceiveJsonAsStringOkHttp");
        String content = "{\"suggestions\":[{\"suggestion\":\"kinase\"},{\"suggestion\":\"kinase kinases\"}]}";

        String searchTerm = "kinas";
        String url = indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

        try (Response response = okHttpClient.newCall(request(url)).execute()) {

            Mono<String> result = Mono.just(response.body().string());
            StepVerifier.create(result)
                    .expectNext(content)
                    .expectComplete()
                    .verify(Duration.ofSeconds(3));

            assertNotNull(result);

            result.subscribe(r -> {
                assertEquals(r, content);
                Assertions.assertThat(r).contains(content);
            });

        }

    }

}
