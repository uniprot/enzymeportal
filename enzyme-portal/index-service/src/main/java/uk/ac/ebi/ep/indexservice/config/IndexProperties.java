package uk.ac.ebi.ep.indexservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.index")
public class IndexProperties {

    private String baseUrl;
    private String enzymeCentricUrl;
    private String proteinCentricUrl;

}
