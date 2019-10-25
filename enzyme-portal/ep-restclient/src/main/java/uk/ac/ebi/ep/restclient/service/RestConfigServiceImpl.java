package uk.ac.ebi.ep.restclient.service;

import java.net.URI;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ep.restclient.config.RestErrorHandler;

/**
 *
 * @author joseph
 */
@Service
public class RestConfigServiceImpl implements RestConfigService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    @Autowired
    public RestConfigServiceImpl(WebClient webClient, RestTemplateBuilder restTemplateBuilder, RestErrorHandler errorHandler) {
        this.webClient = webClient;
        this.restTemplate = restTemplateBuilder
                .errorHandler(errorHandler)
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Override
    public WebClient getWebClient() {
        return webClient;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    public <T> T restHttpRequest(Class<T> clazz, String url) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
        return restTemplate.getForObject(uri, clazz);

    }

}
