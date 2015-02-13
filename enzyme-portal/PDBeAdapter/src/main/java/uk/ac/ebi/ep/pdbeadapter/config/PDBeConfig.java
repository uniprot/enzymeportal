/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.pdbeadapter.PDBeRestService;

/**
 *
 * @author joseph
 */
@Configuration
public class PDBeConfig {

//    @Bean
//    public PdbService pdbService() {
//        return new PdbService();
//    }

    @Bean
    public PDBeRestService pdbeRestService() {

        return new PDBeRestService();
    }
}
