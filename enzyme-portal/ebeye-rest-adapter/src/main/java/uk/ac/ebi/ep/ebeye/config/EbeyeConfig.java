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
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
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
     private final int requestTimeout = 0b111110100;

    @Bean
    public EbeyeRestService ebeyeRestService() {
        return new EbeyeRestService();
    }

//    @Bean
//    public AsyncRestTemplate asyncRestTemplate() {
//        return new AsyncRestTemplate();
//    }
//    @Bean
//    public AsyncRestTemplate asyncRestTemplate() throws IOReactorException {
//
//        // Configure Apache Http Client
//        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager( //
//                new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
//        connectionManager.setMaxTotal(1000); //Total Connections
//        connectionManager.setDefaultMaxPerRoute(900); //Max connections per host
//
//        RequestConfig config = RequestConfig.custom() //
//                .setConnectTimeout(60 * 1000) // milliseconds
//                .build();
//
//        CloseableHttpAsyncClient httpclient = HttpAsyncClientBuilder.create() //
//                .setConnectionManager(connectionManager) //
//                .setDefaultRequestConfig(config) //
//                .build();
//
//        AsyncRestTemplate restTemplate = new AsyncRestTemplate( //
//                new HttpComponentsAsyncClientHttpRequestFactory(httpclient), restTemplate());
//
//        return restTemplate;
//    }

     @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(asyncClientHttpRequestFactory());
    }

     private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
        factory.setReadTimeout(requestTimeout);
        factory.setConnectTimeout(requestTimeout);

        return factory;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

//    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
//        return new HttpComponentsAsyncClientHttpRequestFactory();
//
//    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }

    @Bean
    public EbeyeIndexUrl ebeyeIndexUrl() {
        EbeyeIndexUrl url = new EbeyeIndexUrl();

        String defaultSearchIndexUrl = env.getProperty("ep.default.search.url");
        //String defaultSearchIndexUrl = env.getProperty("ep.default.dev.search.url");

        url.setDefaultSearchIndexUrl(defaultSearchIndexUrl);

        return url;
    }
}
