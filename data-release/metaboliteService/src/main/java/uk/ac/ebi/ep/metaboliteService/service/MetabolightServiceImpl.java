package uk.ac.ebi.ep.metaboliteService.service;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ep.metaboliteService.config.RestErrorHandler;
import uk.ac.ebi.ep.metaboliteService.model.Metabolite;

/**
 *
 * @author joseph
 */
@Service
public class MetabolightServiceImpl implements MetabolightService {

    private static final String METABOLIGHT_URL = "https://www.ebi.ac.uk/metabolights/ws/ebi-internal/";
    private final String ENDPOINT = "check_if_metabolite/";
    private final RestTemplate restTemplate;

    @Autowired
    public MetabolightServiceImpl(RestTemplateBuilder restTemplateBuilder, RestErrorHandler errorHandler) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(errorHandler)
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    private Metabolite serviceHttpRequest(String url) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
        return restTemplate.getForObject(uri, Metabolite.class);

    }

    @Override
    public boolean isMetabolite(String chebiId) {

        Metabolite metabolite = getMetabolite(chebiId);
        return Objects.nonNull(metabolite.getMetabolightsId());
    }

    @Override
    public Metabolite getMetabolite(String chebiId) {

        String serviceUrl = String.format("%s%s%s", METABOLIGHT_URL, ENDPOINT, chebiId);
        return serviceHttpRequest(serviceUrl);
    }

}
