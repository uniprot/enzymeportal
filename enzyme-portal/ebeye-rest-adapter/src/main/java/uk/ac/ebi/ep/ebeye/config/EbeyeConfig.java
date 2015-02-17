/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;

/**
 *
 * @author joseph
 */
@Configuration
public class EbeyeConfig {
    
        @Bean
    public EbeyeRestService ebeyeRestService() {
        return new EbeyeRestService();
    }
}
