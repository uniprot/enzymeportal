package uk.ac.ebi.ep.ebeye.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;

/**
 * Configures the services that interface with the Ebeye search.
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:ebeye.es"})
public class EbeyeConfig {
    @Value("${ep.default.search.url}")
    private String searchUrl;

    @Value("${ebeye.max.ebi.requests}")
    private int maxEbiRequests;

    @Value("${ebeye.chunk.size}")
    private int chunkSize;

    @Value("${request.timeout.millis:5000}")
    private int requestTimeout;

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EbeyeRestService ebeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate,
            AsyncRestTemplate asyncRestTemplate) {
        return new EbeyeRestService(ebeyeIndexUrl, restTemplate, asyncRestTemplate, maxEbiRequests, chunkSize);
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(asyncClientHttpRequestFactory());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(requestTimeout);
        factory.setConnectTimeout(requestTimeout);

        return factory;
    }

    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
        factory.setReadTimeout(requestTimeout);
        factory.setConnectTimeout(requestTimeout);

        return factory;
    }

    @Bean
    public EbeyeIndexUrl ebeyeIndexUrl() {
        EbeyeIndexUrl url = new EbeyeIndexUrl();
        url.setDefaultSearchIndexUrl(searchUrl);
        return url;
    }
}