package uk.ac.ebi.ep.literatureservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.literatureservice.service.PmcRestService;

/**
 *
 * @author joseph
 */
@Configuration
@ComponentScan(basePackages = "uk.ac.ebi.ep")
@PropertySource({"classpath:pmc.configUrl"})
public class PmcConfig {

    @Autowired
    private Environment env;

    @Bean
    public LiteratureService literatureService(PmcRestService pmcRestService) {
        return new LiteratureService(pmcRestService);
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

}
