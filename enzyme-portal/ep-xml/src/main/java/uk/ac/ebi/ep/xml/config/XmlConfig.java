package uk.ac.ebi.ep.xml.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.EnzymeCentric;
import uk.ac.ebi.ep.xml.generator.XmlGenerator;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource(value = "classpath:ep-xml-config.properties", ignoreResourceNotFound = true)
public class XmlConfig {

    @Autowired
    private EnzymePortalXmlService xmlService;
    @Autowired
    private Environment env;

    @Bean(name = "enzymeCentric")
    public XmlGenerator enzymeCentric() {
        return new EnzymeCentric(xmlService);
    }

    @Bean
    public String releaseNumber() {
        return env.getProperty("release.number");
    }

    @Bean
    public String enzymeCentricXmlDir() {
        return env.getProperty("ep.enzyme.centric.xml.dir");
    }

    @Bean
    public String proteinCentricXmlDir() {
        return env.getProperty("ep.protein.centric.xml.dir");
    }

    @Bean
    public String ebeyeXSDs() {
        return env.getProperty("ep.ebeye.xsd");

    }

}
