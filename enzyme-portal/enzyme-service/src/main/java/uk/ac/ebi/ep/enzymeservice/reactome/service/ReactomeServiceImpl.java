package uk.ac.ebi.ep.enzymeservice.reactome.service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.enzymeservice.reactome.config.EnzymeServiceProperties;
import uk.ac.ebi.ep.enzymeservice.reactome.model.Figure;
import uk.ac.ebi.ep.enzymeservice.reactome.model.ReactomeResult;
import uk.ac.ebi.ep.enzymeservice.reactome.model.Summation;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
class ReactomeServiceImpl implements ReactomeService {

    private static final String PATHWAY = "Pathway";

    private final RestConfigService restConfigService;
    private final EnzymeServiceProperties enzymeServiceProperties;
    // private static final String REACTOME_CONTENT_SERVICE_PATH = "/ContentService/data/query/{reactomeId}";
    private static final String REACTOME_CONTENT_SERVICE_PATH = "/ContentService/data/query/";

    @Autowired
    public ReactomeServiceImpl(RestConfigService restConfigService, EnzymeServiceProperties enzymeServiceProperties) {
        this.restConfigService = restConfigService;
        this.enzymeServiceProperties = enzymeServiceProperties;
    }

    private URI buildURI(String reactomeId) {

        String baseUrl = String.format("%s%s%s", enzymeServiceProperties.getReactomeUrl(), REACTOME_CONTENT_SERVICE_PATH, reactomeId);

        return UriComponentsBuilder.fromUriString(baseUrl).build().toUri();
    }

    private Mono<ReactomeResult> reactomeContentService(String id) {

        URI uri = buildURI(id);
        return restConfigService.getWebClient()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new Exception("client error with status : " + response.rawStatusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new Exception("client error with status : " + response.rawStatusCode())))
                .bodyToMono(ReactomeResult.class)
                .switchIfEmpty(Mono.empty())
                .onErrorResume(ex -> Mono.empty());

    }

    @Override
    public Mono<ReactomeResult> findReactomeResultById(String reactomeId) {
        return reactomeContentService(reactomeId);
    }

    @Override
    public Mono<PathWay> findPathwayById(String pathwayId) {
        return reactomeContentService(pathwayId)
                .map(p -> buildPathWay(p));
    }

    @Override
    public Mono<List<PathWay>> findPathwaysByIds(List<String> pathwayIds) {

        List< Mono<ReactomeResult>> data = pathwayIds.stream()
                .map((pathwayId) -> reactomeContentService(pathwayId))
                .collect(Collectors.toList());

        return Flux.merge(data)
                .map(p -> buildPathWay(p))
                .collectList();

    }

    private PathWay buildPathWay(ReactomeResult result) {
        PathWay pathway = null;
        if (result.getSchemaClass() != null && result.getSpeciesName() != null) {
            if (result.getSchemaClass().equalsIgnoreCase(PATHWAY) || result.getSpeciesName().equalsIgnoreCase(PATHWAY)) {
                pathway = new PathWay();
                pathway.setId(result.getStId());
                pathway.setName(result.getDisplayName());
                String url = String.format("%s%s", "https://www.reactome.org/content/detail/", result.getStId());
                pathway.setUrl(url);
                String text = result.getSummation().stream().findFirst().orElse(new Summation()).getText();
                pathway.setDescription(text);
                //image exporter
                //https://reactome.org/ContentService/exporter/diagram/R-HSA-169911.png
                Figure figure = result.getFigure().stream().findFirst().orElse(new Figure());
                String image = String.format("%s%s", "https://www.reactome.org", figure.getUrl()); //"https://www.reactome.org/figures/SAM_formation.jpg"
                if (figure.getUrl() != null) {
                    pathway.setImage(image);
                }

            }
        }
        return pathway;
    }

}
