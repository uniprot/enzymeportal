/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.unisave.config;

import java.util.Date;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Joseph
 */
@Slf4j
public class ShutdownBean {
    
     @PreDestroy
    public void onDestroy() throws Exception {
        System.out.println("Spring Container is destroyed!");
        log.error("destroying spring boot app "+ new Date());
    }
}
