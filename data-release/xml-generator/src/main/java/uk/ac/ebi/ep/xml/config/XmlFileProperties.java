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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ep.xml")
public class XmlFileProperties {

    private String releaseNumber;
    private String enzymeCentric;
    private String proteinCentric;
    private String proteinEntry;
    private String schema;
    private int chunkSize;
    private String dir;
    private int pageSize;
    private String filePermission;

}
