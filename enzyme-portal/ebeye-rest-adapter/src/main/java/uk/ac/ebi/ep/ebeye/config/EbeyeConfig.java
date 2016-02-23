package uk.ac.ebi.ep.ebeye.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.ClientHttpRequestFactory;
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
@PropertySource({"classpath:ebeyeUrl.es"})
public class EbeyeConfig {
    private static final int MAX_EBI_REQUESTS = 100;

    @Value("${ep.default.search.url}")
    private String searchUrl;

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EbeyeRestService ebeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate,
            AsyncRestTemplate asyncRestTemplate) {
        return new EbeyeRestService(ebeyeIndexUrl, restTemplate, asyncRestTemplate, MAX_EBI_REQUESTS);
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Bean
    public EbeyeIndexUrl ebeyeIndexUrl() {
        EbeyeIndexUrl url = new EbeyeIndexUrl();
        url.setDefaultSearchIndexUrl(searchUrl);
        return url;
    }
}