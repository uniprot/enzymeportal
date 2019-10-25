package uk.ac.ebi.ep.enzymeservice.brenda.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.brenda")
public class BrendaProperties {

    private String url;
    private String username;
    private String password;
}
