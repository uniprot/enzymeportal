/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.dataconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.data.service.BioPortalService;
import uk.ac.ebi.ep.data.service.DiseaseParser;
import uk.ac.ebi.ep.data.service.DiseaseService;
import uk.ac.ebi.ep.data.service.EnzymePortalCompoundService;
import uk.ac.ebi.ep.data.service.UniprotEntryService;

/**
 *
 * @author joseph
 */
@Configuration
public class GlobalDataConfig {
    
        @Bean
    public DiseaseService diseaseService() {
        return new DiseaseService();
    }

    @Bean
    public DiseaseParser diseaseParser() {
        return new DiseaseParser();
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
    public EnzymePortalCompoundService enzymePortalCompoundService() {
        return new EnzymePortalCompoundService();
    }
    

}
