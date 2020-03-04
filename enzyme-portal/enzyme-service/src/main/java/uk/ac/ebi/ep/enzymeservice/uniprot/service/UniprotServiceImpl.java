package uk.ac.ebi.ep.enzymeservice.uniprot.service;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.enzymeservice.uniprot.config.UniprotServiceProperties;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Comment;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Kinetics;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.PhDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.TemperatureDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.UniprotApi;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author joseph
 */
@Service
public class UniprotServiceImpl implements UniprotService {

    private static final String TYPE = "BIOPHYSICOCHEMICAL_PROPERTIES";
    private static final String FORMAT = "&format=json";
    private static final String PROTEIN_ENDPOINT = "/api/proteins?offset=0&size=10&accession=";
    private static final String EC_ENDPOINT = "/api/proteins?offset=0&size=1000&reviewed=true&ec=";
    private final RestConfigService restConfigService;
    private final UniprotServiceProperties uniprotServiceProperties;

    @Autowired
    public UniprotServiceImpl(RestConfigService restConfigService, UniprotServiceProperties uniprotServiceProperties) {
        this.restConfigService = restConfigService;
        this.uniprotServiceProperties = uniprotServiceProperties;
    }

    private URI buildURI(String accession) {

        String baseUrl = String.format("%s%s%s%s", uniprotServiceProperties.getUrl(), PROTEIN_ENDPOINT, accession, FORMAT);

        return UriComponentsBuilder.fromUriString(baseUrl).build().toUri();
    }

    private URI buildURI(String queryTerm, String endpoint) {

        String baseUrl = String.format("%s%s%s%s", uniprotServiceProperties.getUrl(), endpoint, queryTerm, FORMAT);

        return UriComponentsBuilder.fromUriString(baseUrl).build().toUri();
    }

    private Flux<UniprotApi> uniprotNonBlockingService(String id) {

        URI uri = buildURI(id);
        return restConfigService.getWebClient()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new Exception("client error with status : " + response.rawStatusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new Exception("client error with status : " + response.rawStatusCode())))
                .bodyToFlux(UniprotApi.class)
                .switchIfEmpty(Flux.empty())
                .onErrorResume(ex -> Flux.empty());

    }

    public UniprotApi uniprotApiByAccessionBlocking(String accession) {
        return uniprotNonBlockingService(accession)
                .single(new UniprotApi()).block();

    }

    private List<UniprotApi> responseEntity(String accession) {
        URI uri = buildURI(accession);
        ResponseEntity<List<UniprotApi>> data
                = restConfigService.getRestTemplate().exchange(uri,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UniprotApi>>() {
                });

        if (data.getBody() != null && data.hasBody()) {
            return data.getBody();
        }
        return Collections.EMPTY_LIST;
    }

    private List<UniprotApi> responseEntity(String queryTerm, String endpoint) {
        URI uri = buildURI(queryTerm, endpoint);
        ResponseEntity<List<UniprotApi>> data
                = restConfigService.getRestTemplate().exchange(uri,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UniprotApi>>() {
                });

        if (data.getBody() != null && data.hasBody()) {
            return data.getBody();
        }
        return Collections.EMPTY_LIST;
    }

    private UniprotApi byResponseEntity(String accession) {

        return responseEntity(accession)
                .stream()
                .findFirst()
                .orElse(new UniprotApi());
    }

    @Override
    public UniprotApi uniprotApiByAccession(String accession) {

        UniprotApi api = uniprotApiByAccessionBlocking(accession);
        if (api == null) {
            api = byResponseEntity(accession);
        }
        return api;
    }

    @Override
    public List<UniprotApi> uniprotApiByEc(String ec) {

        return responseEntity(ec, EC_ENDPOINT);

    }

    @Override
    public Comment findKinectParamsCommentByAccession(String accession) {

        return uniprotApiByAccession(accession).getComments()
                .stream()
                .filter(obj -> Objects.nonNull(obj))
                .filter(type -> type.getType().equalsIgnoreCase(TYPE))
                .findAny().orElse(new Comment());
    }

    @Override
    public Kinetics findKineticsByAccession(String accession) {
        return findKinectParamsCommentByAccession(accession).getKinetics();
    }

    @Override
    public List<PhDependence> findPhDependenceByAccession(String accession) {
        return findKinectParamsCommentByAccession(accession).getPhDependence();
    }

    @Override
    public List<TemperatureDependence> findTemperatureDependenceByAccession(String accession) {
        return findKinectParamsCommentByAccession(accession).getTemperatureDependence();
    }

}
