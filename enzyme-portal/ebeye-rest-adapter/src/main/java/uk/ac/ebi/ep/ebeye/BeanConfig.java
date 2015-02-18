/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.ebeye;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Configuration
public class BeanConfig {
    
        @Bean
    public EbeyeService ebeyeService(){
     return new EbeyeService();
    }
}
