package uk.ac.ebi.ep.indexservice.service;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.config.IndexProperties;
import uk.ac.ebi.ep.indexservice.exceptions.IndexServiceException;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Autocomplete;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Suggestion;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author Joseph
 */
@Service
@Slf4j
public class SuggestionServiceImpl implements SuggestionService {

    private final IndexProperties indexProperties;

    private static final String QUERY_PARAM = "term";
    private static final String FORMAT = "&format=json";
    private static final String ENDPOINT = "/autocomplete";

    private final RestConfigService restConfigService;
    private static final String MSG = "autocomplete search term : ";

    @Autowired
    public SuggestionServiceImpl(IndexProperties properties, RestConfigService restConfigService) {

        this.indexProperties = properties;
        this.restConfigService = restConfigService;
    }

    private URI buildURI(String searchTerm) {
        log.debug(MSG + searchTerm);
        if (Objects.nonNull(searchTerm)) {
            searchTerm = searchTerm.toLowerCase();
        }
        return UriComponentsBuilder
                .fromUriString(indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl() + ENDPOINT)
                .queryParam(QUERY_PARAM, "{searchTerm}")
                .query(FORMAT)
                .build(searchTerm);

    }

    @Override
    public Mono<Autocomplete> autocomplete(String searchTerm) {
        log.debug(MSG + searchTerm);
        WebClient webclient = restConfigService.getWebClient();

        return webclient.get()
                .uri(buildURI(searchTerm))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(Autocomplete.class))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Autocomplete> autocompleteSearch(String searchTerm) {
        log.debug(MSG + searchTerm);
        WebClient webclient = restConfigService.getWebClient();

        return webclient.get()
                .uri(buildURI(searchTerm))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new IndexServiceException("client error with status : " + response.rawStatusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> response.bodyToMono(IndexServiceException.class))
                .bodyToMono(Autocomplete.class)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<Suggestion> suggestions(String searchTerm) {
        log.debug(MSG + searchTerm);
        WebClient webclient = restConfigService.getWebClient();
        return webclient.get()
                .uri(buildURI(searchTerm))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(Autocomplete.class))
                .flatMapIterable(Autocomplete::getSuggestions)
                .onBackpressureBuffer()
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public List<Suggestion> getSuggestions(String searchTerm) {

        return autocomplete(searchTerm)
                .blockOptional()
                .orElse(new Autocomplete())
                .getSuggestions();
    }

    @Override
    public List<Suggestion> findSuggestions(String searchTerm) {
        return autocompleteSearch(searchTerm)
                .blockOptional()
                .orElse(new Autocomplete()).getSuggestions();

    }

}
