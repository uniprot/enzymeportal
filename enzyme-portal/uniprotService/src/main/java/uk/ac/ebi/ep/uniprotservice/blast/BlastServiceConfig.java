/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.blast;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.ebiservice.UniProtBlastService;

/**
 *
 * @author joseph
 */
@Configuration
public class BlastServiceConfig {
    
        @Bean
    public UniProtBlastService uniprotBlastService() {
        ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
        UniProtBlastService uniprotBlastService = serviceFactoryInstance.getUniProtBlastService();
        return uniprotBlastService;
    }

}
