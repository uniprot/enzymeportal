/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.dataconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.data.service.BioPortalService;
import uk.ac.ebi.ep.data.service.DiseaseService;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.RelatedProteinsService;
import uk.ac.ebi.ep.data.service.UniprotEntryService;

/**
 *
 * @author joseph
 */
@Configuration
public class GlobalConfig {

    @Autowired
    private Environment env;
    @Bean
    public DiseaseService diseaseService() {
        return new DiseaseService();
    }

    @Bean
    public BioPortalService bioPortalService() {
        return new BioPortalService();
    }

    @Bean
    public UniprotEntryService uniprotEntryService() {
        return new UniprotEntryService();
    }

    @Bean
    public EnzymePortalService enzymePortalService() {
        return new EnzymePortalService();
    }
    
    @Bean
    public RelatedProteinsService relatedProteinsService(){
        return new RelatedProteinsService();
    }

//    @Bean
//    public Config config() {
//        Config config = new Config();
//        config.setResultsPerPage(Integer.parseInt(env.getProperty("search.results.per.page")));
//        config.setMaxPages(Integer.parseInt(env.getProperty("search.results.pages.max")));
//        config.setMaxTextLength(Integer.parseInt(env.getProperty("search.summary.text.length.max")));
//        config.setMaxMoleculesPerGroup(Integer.parseInt(env.getProperty("search.molecules.group.max")));
//        config.setSearchCacheSize(Integer.parseInt(env.getProperty("search.cache.size")));
//        return config;
//    }


//    @Bean
//    IntenzConfig intenzConfig() {
//    IntenzConfig config = new IntenzConfig();
//      
//    config.setTimeout(Integer.parseInt(env.getProperty("intenz.ws.timeout")));
//    config.setIntenzXmlUrl(env.getProperty("intenz.xml.url"));
//        return config;
//    }

}
