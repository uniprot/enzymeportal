package uk.ac.ebi.ep.indexservice.service;

import java.net.URI;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.exceptions.IndexServiceException;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;

/**
 *
 * @author joseph
 * @param <T>
 */
@Slf4j
public abstract class AbstractIndexService<T> {

    String query = "query";
    String queryParam = "{queryParam}";
    String queryString = "&facetcount=%d&facets=%s&start=%d&size=%d&fields=%s&sort=%s&reverse=%s&format=%s";
    String queryStringNoFacet = "&facetcount=%d&start=%d&size=%d&fields=%s&sort=%s&reverse=%s&format=%s";

    protected URI buildURI(QueryBuilder queryBuilder, String indexUrl) {
        log.debug("search term $ " + queryBuilder.getQuery());
        String fields = queryBuilder.getFields().stream().collect(Collectors.joining(","));
        String q = String.format(queryStringNoFacet,
                queryBuilder.getFacetcount(), queryBuilder.getStart(), queryBuilder.getSize(), fields, queryBuilder.getSort(), queryBuilder.getReverse(), queryBuilder.getFormat());

        if (!StringUtils.isEmpty(queryBuilder.getFacets()) && StringUtils.hasText(queryBuilder.getFacets())) {
            q = String.format(queryString,
                    queryBuilder.getFacetcount(), queryBuilder.getFacets(), queryBuilder.getStart(), queryBuilder.getSize(), fields, queryBuilder.getSort(), queryBuilder.getReverse(), queryBuilder.getFormat());

        }

        return UriComponentsBuilder
                .fromUriString(indexUrl)
                .queryParam(this.query, queryParam)
                .query(q)
                .build(queryBuilder.getQuery());

    }

    @Deprecated
    protected Mono<T> searchIndexXX(WebClient webClient, QueryBuilder queryBuilder, String indexUrl, Class<T> resultType) {
        URI uri = buildURI(queryBuilder, indexUrl);
        log.debug("Request URL : " + uri.toString());
        return webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new IndexServiceException("client error with status : " + response.rawStatusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> response.bodyToMono(IndexServiceException.class))
                .bodyToMono(resultType)
                .switchIfEmpty(Mono.empty());

    }

    protected Mono<T> searchIndex(WebClient webClient, QueryBuilder queryBuilder, String indexUrl, Class<T> resultType) {
        URI uri = buildURI(queryBuilder, indexUrl);
        log.debug("Request URL : " + uri.toString());
        return webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(resultType))
                .switchIfEmpty(Mono.empty());

    }

    protected Flux<T> indexSearch(WebClient webClient, QueryBuilder queryBuilder, String indexUrl, Class<T> resultType) {
        URI uri = buildURI(queryBuilder, indexUrl);
        log.debug("Request URL : " + uri.toString());
        return webClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(resultType))
                .switchIfEmpty(Flux.empty());

    }

    protected <T> T searchIndex(RestTemplate restTemplate, QueryBuilder queryBuilder, String indexUrl, Class<T> resultType) {
        URI uri = buildURI(queryBuilder, indexUrl);
        log.info("Request URL : " + uri.toString());

        return restTemplate.getForObject(uri, resultType);
    }

}
