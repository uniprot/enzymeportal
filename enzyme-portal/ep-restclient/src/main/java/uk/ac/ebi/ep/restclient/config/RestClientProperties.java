package uk.ac.ebi.ep.restclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.config")
public class RestClientProperties {

    private String proxyHost;
    private int proxyPort;
    private boolean useProxy;
}
