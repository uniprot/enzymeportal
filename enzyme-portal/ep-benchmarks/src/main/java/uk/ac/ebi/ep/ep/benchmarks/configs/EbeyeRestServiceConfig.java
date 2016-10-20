/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ep.benchmarks.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.EbeyeQueryService;
import uk.ac.ebi.ep.ebeye.EbeyeQueryServiceImpl;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EbeyeSuggestionService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
@PropertySource({"classpath:ebeye.es"})
public class EbeyeRestServiceConfig {
    
    private static final int REQUEST_TIMEOUT_MILLIS = 60000;

    @Autowired
    private Environment env;

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        //return new AsyncRestTemplate(asyncClientHttpRequestFactory());
        return new AsyncRestTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(REQUEST_TIMEOUT_MILLIS);
        factory.setConnectTimeout(REQUEST_TIMEOUT_MILLIS);

        return factory;
    }

    @Bean
    public EbeyeRestService ebeyeRestService(EbeyeQueryService proteinQueryService) {
        return new EbeyeRestService(proteinQueryService);
    }

    @Bean
    public EbeyeQueryService proteinQueryService(EbeyeIndexProps proteinCentricProps, RestTemplate restTemplate) {
        return new EbeyeQueryServiceImpl(proteinCentricProps, restTemplate);
    }

    @Bean
    public EnzymeCentricService enzymeCentricService(RestTemplate restTemplate, EbeyeIndexProps enzymeCentricProps) {
        return new EnzymeCentricService(restTemplate, enzymeCentricProps);
    }

    @Bean
    public EbeyeQueryService enzymeQueryService(EbeyeIndexProps enzymeCentricProps, RestTemplate restTemplate) {
        return new EbeyeQueryServiceImpl(enzymeCentricProps, restTemplate);
    }

    @Bean
    public EbeyeIndexProps ebeyeIndexProps() {
        int maxEbiRequests = Integer.parseInt(env.getProperty("ebeye.max.ebi.requests"));
        int chunkSize = Integer.parseInt(env.getProperty("ebeye.chunk.size"));
        String enzymeCentricUrl = env.getProperty("ep.enzyme.centric.search.url");
        String proteinCentricUrl = env.getProperty("ep.protein.centric.search.url");

        EbeyeIndexProps url = new EbeyeIndexProps();

        url.setProteinCentricSearchUrl(proteinCentricUrl);
        url.setEnzymeCentricSearchUrl(enzymeCentricUrl);
        url.setChunkSize(chunkSize);
        url.setMaxEbiSearchLimit(maxEbiRequests);

        return url;
    }

    @Bean
    public EbeyeSuggestionService ebeyeSuggestionService() {
        return new EbeyeSuggestionService(ebeyeIndexProps(), restTemplate());
    }

    @Bean
    public EbeyeSuggestionService ebeyeSuggestionService(EbeyeIndexProps enzymeCentricProps, RestTemplate restTemplate) {
        return new EbeyeSuggestionService(enzymeCentricProps, restTemplate);
    }

}
