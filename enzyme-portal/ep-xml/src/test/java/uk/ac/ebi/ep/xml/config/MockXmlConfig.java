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
package uk.ac.ebi.ep.xml.config;

import java.io.File;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.EnzymeCentric;
import uk.ac.ebi.ep.xml.generator.XmlGenerator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
public class MockXmlConfig {


    @Bean(name = "enzymeCentric")
    public XmlGenerator enzymeCentric() {
        return new EnzymeCentric(enzymePortalXmlService(), xmlConfigParams());
    }
    @Bean
    public EnzymePortalXmlService enzymePortalXmlService(){
        return new EnzymePortalXmlService();
    }

    @Bean
    public XmlConfigParams xmlConfigParams() {
        XmlConfigParams params = new XmlConfigParams();
        params.setChunkSize(chunkSize());
        params.setEbeyeXSDs(ebeyeXSDs());
        params.setEnzymeCentricXmlDir(enzymeCentricXmlDir());
        params.setProteinCentricXmlDir(proteinCentricXmlDir());
        params.setReleaseNumber(releaseNumber());

        return params;
    }

    public String releaseNumber() {
        return "2016_04";
    }

    public String enzymeCentricXmlDir() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "ebeye.xml";
    }

    public String proteinCentricXmlDir() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "4ebeies.xml";
    }

    public String ebeyeXSDs() {
        return "http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd";

    }

    private int chunkSize() {
        return 10;
    }
}
