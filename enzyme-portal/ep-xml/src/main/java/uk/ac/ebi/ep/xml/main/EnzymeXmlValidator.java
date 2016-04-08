/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.main;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.xml.config.XmlConfig;
import uk.ac.ebi.ep.xml.generator.EnzymeCentric;
import uk.ac.ebi.ep.xml.generator.XmlGenerator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeXmlValidator {

    private static final Logger logger = Logger.getLogger(EnzymeXmlValidator.class);

    private EnzymeXmlValidator() {

    }

    public static void main(String[] args) throws Exception {

        String profile = "uzpdev";//validation is meant to be done in development areas hence the default database param.

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(DataConfig.class, XmlConfig.class, GlobalConfig.class);
        context.scan("uk.ac.ebi.ep.data.dataconfig", "uk.ac.ebi.ep.xml.config");
        context.refresh();

        XmlGenerator xmlGenerator = context.getBean(EnzymeCentric.class);
        xmlGenerator.validateXML();
        logger.info("Enzyme-centric XML Validation is complete. Please check the logs.");

    }
}
