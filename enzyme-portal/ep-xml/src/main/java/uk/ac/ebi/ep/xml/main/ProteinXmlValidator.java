/*
 * Copyright 2016 EMBL-EBI.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.ep.xml.main;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.model.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.xml.config.XmlConfig;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinXmlValidator {

    private static final Logger logger = Logger.getLogger(ProteinXmlValidator.class);

    private ProteinXmlValidator() {

    }

    public static void main(String[] args) throws Exception {

        String profile = "uzprel";//xml generation is meant to be from Rel hence the default database param.

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(DataConfig.class, XmlConfig.class, GlobalConfig.class);
        context.scan("uk.ac.ebi.ep.config", "uk.ac.ebi.ep.xml.config");
        context.refresh();

        XmlConfigParams xmlConfigParams = context.getBean(XmlConfigParams.class);

        String xmlFile = xmlConfigParams.getProteinCentricXmlDir();
        String[] xsdFiles = xmlConfigParams.getEbeyeXSDs().split(",");

        EnzymePortalXmlValidator.validateXml(xmlFile, xsdFiles);
        logger.info("Protein-centric XML Validation is complete. Please check the logs.");

    }
}
