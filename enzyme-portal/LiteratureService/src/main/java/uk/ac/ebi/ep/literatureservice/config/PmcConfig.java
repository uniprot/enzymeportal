/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.literatureservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.literatureservice.service.PmcRestService;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:pmc.configUrl"})
public class PmcConfig {

    @Autowired
    private Environment env;

    @Bean
    public LiteratureService literatureService() {
        return new LiteratureService();
    }

    @Bean
    public PmcRestService pmcRestService() {
        return new PmcRestService();
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
