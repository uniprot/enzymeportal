/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;


/**
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:ebeyeUrl.es"})
public class EbeyeConfig {

    @Autowired
    private Environment env;

    @Bean
    public EbeyeRestService ebeyeRestService() {
        return new EbeyeRestService();
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
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        return factory;
    }

    @Bean
    public EbeyeIndexUrl ebeyeIndexUrl() {
        EbeyeIndexUrl url = new EbeyeIndexUrl();

        String defaultSearchIndexUrl = env.getProperty("ep.default.search.url");

        url.setDefaultSearchIndexUrl(defaultSearchIndexUrl);

        return url;
    }
}
