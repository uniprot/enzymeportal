package uk.ac.ebi.ep.xml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class XmlDatasourceProperties {

    private String url;
    private String username;
    private String password;
}
