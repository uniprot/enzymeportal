package uk.ac.ebi.ep.enzymeservice.reactome.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.enzymes")
public class EnzymeServiceProperties {

    private String reactomeUrl;
//    private String proxyHost;
//    private int proxyPort;

}
