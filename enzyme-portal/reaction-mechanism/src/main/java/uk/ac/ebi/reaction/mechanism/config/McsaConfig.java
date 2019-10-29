package uk.ac.ebi.reaction.mechanism.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.reaction.mechanism.service.ReactionMechanismService;
import uk.ac.ebi.reaction.mechanism.service.ReactionMechanismServiceImpl;

/**
 *
 * @author Joseph
 */
@Configuration
@ComponentScan(basePackages = "uk.ac.ebi.ep")
@PropertySource({"classpath:mcsa.urlConfig"})
public class McsaConfig {

    @Bean
    @Autowired
    public ReactionMechanismUrl reactionMechanismUrl(Environment env) {
        ReactionMechanismUrl url = new ReactionMechanismUrl();
        url.setMcsaUrl(env.getProperty("mcsa.url"));
        return url;

    }

    @Bean
    public ReactionMechanismService reactionMechanismService(ReactionMechanismUrl reactionMechanismUrl, RestTemplate restTemplate) {
        return new ReactionMechanismServiceImpl(reactionMechanismUrl, restTemplate);
    }

}
