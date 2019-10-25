package uk.ac.ebi.ep.restclient.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author joseph
 */
public interface RestConfigService {

    WebClient getWebClient();

    RestTemplate getRestTemplate();

    <T> T restHttpRequest(Class<T> clazz, String url);
}
