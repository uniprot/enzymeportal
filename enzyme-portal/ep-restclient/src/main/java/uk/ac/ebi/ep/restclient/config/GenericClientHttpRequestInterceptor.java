package uk.ac.ebi.ep.restclient.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author joseph
 */
@Slf4j
public class GenericClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        log.debug("Headers: {}", request.getHeaders());
        log.debug("Request Method: {}", request.getMethod());
        log.debug("Request URI: {}", request.getURI());

        return execution.execute(request, body);
    }

}
