package uk.ac.ebi.ep.restapi.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.openapi.springdoc")
public class ApiProperties {

    private String contactName;
    private String contactEmail;
    private String licenseName;
    private String licenseUrl;
    private String serverUrl;
    private String serverDesc;
    private String apiTitle;
    private String apiDesc;
    private String apiVersion;
    private String apiTos;

}
