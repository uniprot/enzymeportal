package uk.ac.ebi.ep.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.COLLECTION_JSON;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL_FORMS;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.UBER;
import uk.ac.ebi.ep.restapi.controller.ProteinController;
import uk.ac.ebi.ep.restapi.hateoas.ProteinModelAssembler;
import uk.ac.ebi.ep.restapi.model.ProteinModel;

/**
 *
 * @author joseph
 */
@EnableHypermediaSupport(type = {HAL, HAL_FORMS, COLLECTION_JSON, UBER})
@Configuration
public class HypermediaConfig {

    @Bean
    public ProteinModelAssembler proteinModelAssembler() {

        return new ProteinModelAssembler(ProteinController.class, ProteinModel.class);
    }
}
