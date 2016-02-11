/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.dataconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import uk.ac.ebi.ep.data.service.AnalysisService;
import uk.ac.ebi.ep.data.service.BioPortalService;
import uk.ac.ebi.ep.data.service.DiseaseService;
import uk.ac.ebi.ep.data.service.EnzymePortalParserService;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.EnzymePortalSummaryService;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.data.service.RelatedProteinsService;
import uk.ac.ebi.ep.data.service.UniprotEntryService;

/**
 *
 * @author joseph
 */
@Configuration
public class GlobalConfig {

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
    public RelatedProteinsService relatedProteinsService() {
        return new RelatedProteinsService();
    }

    @Bean
    public EnzymePortalSummaryService enzymePortalSummaryService() {
        return new EnzymePortalSummaryService();
    }

    @Bean
    public EnzymePortalXmlService enzymePortalXmlService() {
        return new EnzymePortalXmlService();
    }

    @Bean
    public EnzymePortalParserService enzymePortalParserService() {
        return new EnzymePortalParserService();
    }

    @Bean
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    @Bean
    public AnalysisService analysisService() {
        return new AnalysisService();
    }

}
