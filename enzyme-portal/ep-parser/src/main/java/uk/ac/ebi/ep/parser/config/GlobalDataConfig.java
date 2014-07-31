/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.parser.parsers.DiseaseParser;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalCompoundParser;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalPathwaysParser;

/**
 *
 * @author joseph
 */
@Configuration
public class GlobalDataConfig {


    @Bean
    public DiseaseParser diseaseParser() {
        return new DiseaseParser();
    }



    @Bean
    public EnzymePortalCompoundParser enzymePortalCompoundParser() {
        return new EnzymePortalCompoundParser();
    }

    @Bean
    public EnzymePortalPathwaysParser enzymePortalPathwaysParser() {
        return new EnzymePortalPathwaysParser();
    }

}
