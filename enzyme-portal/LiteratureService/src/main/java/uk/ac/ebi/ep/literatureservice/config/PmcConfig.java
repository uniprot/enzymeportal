package uk.ac.ebi.ep.literatureservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.literatureservice.service.PmcRestService;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:pmc.configUrl"})
public class PmcConfig {

    private static final int REQUEST_TIMEOUT_MILLIS = 60000;
    @Autowired
    private Environment env;

    @Bean
    public LiteratureService literatureService(PmcRestService pmcRestService) {
        return new LiteratureService(pmcRestService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public PmcRestService pmcRestService(PmcServiceUrl pmcServiceUrl, RestTemplate restTemplate) {
        return new PmcRestService(pmcServiceUrl, restTemplate);
    }

    @Bean
    public PmcServiceUrl pmcServiceUrl() {
        PmcServiceUrl serviceUrl = new PmcServiceUrl();

        String accessionUrl = env.getProperty("pmc.specific.url");
        String genericUrl = env.getProperty("pmc.generic.url");

        serviceUrl.setSpecificUrl(accessionUrl);
        serviceUrl.setGenericUrl(genericUrl);

        return serviceUrl;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(REQUEST_TIMEOUT_MILLIS);
        factory.setConnectTimeout(REQUEST_TIMEOUT_MILLIS);

        return factory;
    }
}
