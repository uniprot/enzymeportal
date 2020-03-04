package uk.ac.ebi.ep.enzymeservice.uniprot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.uniprot")
public class UniprotServiceProperties {

    private String url;
}
