package uk.ac.ebi.ep.enzymeservice.intenz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.intenz")
public class IntenzProperties {

    private int timeout;

    private String intenzXmlUrl;
    private String ecBaseUrl;

}
