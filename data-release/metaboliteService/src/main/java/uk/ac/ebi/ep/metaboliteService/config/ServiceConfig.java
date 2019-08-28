package uk.ac.ebi.ep.metaboliteService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;

/**
 *
 * @author joseph
 */
@Configuration
public class ServiceConfig {

    @Bean
    public ChebiWebServiceClient chebiWebServiceClient() {

        return new ChebiWebServiceClient();
    }
}
