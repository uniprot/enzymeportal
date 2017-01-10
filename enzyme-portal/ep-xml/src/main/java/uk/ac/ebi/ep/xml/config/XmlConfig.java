package uk.ac.ebi.ep.xml.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.EnzymeCentric;
import uk.ac.ebi.ep.xml.generator.XmlGenerator;

/**
 * main configuration for this module
 *
 * @author joseph
 */
@Configuration
@Import({EnzymePortalXmlService.class})
@PropertySource(value = "classpath:ep-xml-config.properties", ignoreResourceNotFound = true)
public class XmlConfig {
    @Autowired
    private Environment env;

    @Autowired
    private EnzymePortalXmlService enzymePortalXmlService;

    @Bean(name = "enzymeCentric")
    public XmlGenerator enzymeCentric() {
        return new EnzymeCentric(enzymePortalXmlService, xmlConfigParams());
    }

    @Bean
    public XmlConfigParams xmlConfigParams() {
        XmlConfigParams params = new XmlConfigParams();
        params.setChunkSize(chunkSize());
        params.setEbeyeXSDs(ebeyeXSDs());
        params.setEnzymeCentricXmlDir(enzymeCentricXmlDir());
        params.setProteinCentricXmlDir(proteinCentricXmlDir());
        params.setReleaseNumber(releaseNumber());
        params.setXmlDir(xmlDir());

        return params;
    }

    public String releaseNumber() {
        return env.getProperty("release.number");
    }

    public String enzymeCentricXmlDir() {
        return env.getProperty("ep.enzyme.centric.xml.dir");
    }

    public String proteinCentricXmlDir() {
        return env.getProperty("ep.protein.centric.xml.dir");
    }

    public String ebeyeXSDs() {
        return env.getProperty("ep.ebeye.xsd");
    }

    private int chunkSize() {
        return Integer.parseInt(env.getProperty("ep.protein.centric.chunk"));
    }
    
    private String xmlDir(){
        return env.getProperty("ep.xml.dir");
    }
}