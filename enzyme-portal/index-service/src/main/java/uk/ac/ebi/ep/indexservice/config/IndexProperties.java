package uk.ac.ebi.ep.indexservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Joseph
 */
@Configuration
@ConfigurationProperties(prefix = "ep.index")
public class IndexProperties {

    private String baseUrl;
    private String enzymeCentricUrl;
    private String proteinCentricUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getEnzymeCentricUrl() {
        return enzymeCentricUrl;
    }

    public void setEnzymeCentricUrl(String enzymeCentricUrl) {
        this.enzymeCentricUrl = enzymeCentricUrl;
    }

    public String getProteinCentricUrl() {
        return proteinCentricUrl;
    }

    public void setProteinCentricUrl(String proteinCentricUrl) {
        this.proteinCentricUrl = proteinCentricUrl;
    }

}
