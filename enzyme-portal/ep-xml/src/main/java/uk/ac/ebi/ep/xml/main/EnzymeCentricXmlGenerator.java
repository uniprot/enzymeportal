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
public class EnzymeCentricXmlGenerator {

    private static final Logger logger = Logger.getLogger(EnzymeCentricXmlGenerator.class);

    private EnzymeCentricXmlGenerator() {

    }

    public static void main(String[] args) throws Exception {

        String profile;
        //profile = "";

        if (args == null || args.length == 0) {
            logger.error("Please provide required parameters");
            System.exit(0);
        }

        if (args.length == 1) {

            profile = args[0];

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.getEnvironment().setActiveProfiles(profile);
            context.register(DataConfig.class, XmlConfig.class, GlobalConfig.class);
            context.scan("uk.ac.ebi.ep.data.dataconfig", "uk.ac.ebi.ep.xml.config");
            context.refresh();

            XmlGenerator xmlGenerator = context.getBean(EnzymeCentric.class);
            xmlGenerator.generateXmL();
            xmlGenerator.validateXML();

        }

    }
}
