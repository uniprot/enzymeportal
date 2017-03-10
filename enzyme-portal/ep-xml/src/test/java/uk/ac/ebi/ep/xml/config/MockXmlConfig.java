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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
@PropertySource(value = "classpath:test-xml-config.ep", ignoreResourceNotFound = true)
public class MockXmlConfig {

    @Autowired
    private Environment env;

    @Bean
    public EnzymePortalXmlService xmlService() {
        return new EnzymePortalXmlService();
    }

    @Bean(name = "mockXmlConfigParams")
    public XmlConfigParams mockXmlConfigParams() {
        XmlConfigParams params = new XmlConfigParams();
        params.setChunkSize(chunkSize());
        params.setEbeyeXSDs(ebeyeXSDs());
        params.setEnzymeCentricXmlDir(enzymeCentricXmlDir());
        params.setProteinCentricXmlDir(proteinCentricXmlDir());
        params.setReleaseNumber(releaseNumber());

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
        return "http://www.ebi.ac.uk/ebisearch/XML4dbDumps.xsd";

    }

    private int chunkSize() {
        return 10;
    }
}
