package uk.ac.ebi.ep.model.dataconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.model.service.EnzymePortalXmlService;

/**
 *
 * @author joseph
 */
@Configuration
public class GlobalConfig {
   


    @Bean
    public EnzymePortalXmlService enzymePortalXmlService() {
        return new EnzymePortalXmlService();
    }

    @Bean
    public EnzymePortalParserService enzymePortalParserService() {
        return new EnzymePortalParserService();
    }


    
}
