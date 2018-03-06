/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.ac.ebi.ep.xml.config.XmlFileProperties;
import uk.ac.ebi.ep.xml.repository.EnzymePortalUniqueEcRepository;
import uk.ac.ebi.ep.xml.service.XmlService;
import uk.ac.ebi.ep.xml.service.XmlServiceImpl;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
@Import(XmlFileProperties.class)
public class ServiceConfig {

    @Autowired
    private EnzymePortalUniqueEcRepository enzymePortalUniqueEcRepository;

    @Autowired
    private XmlFileProperties xmlFileProperties;

    @Bean
    public XmlService xmlService() {
        return new XmlServiceImpl(enzymePortalUniqueEcRepository);
    }

    @Bean
    public XmlStreamingService xmlStreamingService() {
        return new XmlStreamingService(xmlService(), xmlFileProperties);
    }
}
