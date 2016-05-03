/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;

/**
 *
 * @author joseph
 */
@Configuration
public class EnzymesConfig {

    @Autowired
    private Environment env;

    @Bean
    public ChebiConfig chebiConfig() {
        ChebiConfig chebiConfig = new ChebiConfig();
        chebiConfig.setTimeout(Integer.parseInt(env.getProperty("chebi.ws.timeout")));
        chebiConfig.setMaxThreads(Integer.parseInt(env.getProperty("chebi.threads.max")));
        chebiConfig.setSearchStars(env.getProperty("chebi.search.stars"));
        chebiConfig.setMaxRetrievedMolecules(Integer.parseInt(env.getProperty("chebi.results.max")));
        chebiConfig.setCompoundBaseUrl(env.getProperty("chebi.compound.base.url"));
        chebiConfig.setCompoundImgBaseUrl(env.getProperty("chebi.compound.img.base.url"));

        return chebiConfig;
    }

    
//    @Bean
//    public EbeyeRestService ebeyeRestService() {
//        //return new EbeyeRestService();
//        System.out.println("SERVICE URL "+ ebeyeIndexUrl().getDefaultSearchIndexUrl());
//            return  new EbeyeRestService(asyncRestTemplate(),ebeyeIndexUrl(), restTemplate());
//    }

//    @Bean
//    public EbeyeIndexUrl ebeyeIndexUrl() {
//        EbeyeIndexUrl url = new EbeyeIndexUrl();
//        url.setDefaultSearchIndexUrl(env.getProperty("ep.default.search.url"));
//        return url;
//    }
//
//    @Bean
//    public AsyncRestTemplate asyncRestTemplate() {
//        return new AsyncRestTemplate();
//    }
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(clientHttpRequestFactory());
//    }
//
//    private ClientHttpRequestFactory clientHttpRequestFactory() {
//        return new HttpComponentsClientHttpRequestFactory();
//        
//    }
    

    
    @Bean
    public LiteratureService literatureService(){
        return new LiteratureService();
    }

}
