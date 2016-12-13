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

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class XmlConfigParams {

    private String releaseNumber;
    private String enzymeCentricXmlDir;
    private String proteinCentricXmlDir;
    private String ebeyeXSDs;
    private int chunkSize;
    private String xmlDir;

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getEnzymeCentricXmlDir() {
        return enzymeCentricXmlDir;
    }

    public void setEnzymeCentricXmlDir(String enzymeCentricXmlDir) {
        this.enzymeCentricXmlDir = enzymeCentricXmlDir;
    }

    public String getProteinCentricXmlDir() {
        return proteinCentricXmlDir;
    }

    public void setProteinCentricXmlDir(String proteinCentricXmlDir) {
        this.proteinCentricXmlDir = proteinCentricXmlDir;
    }

    public String getEbeyeXSDs() {
        return ebeyeXSDs;
    }

    public void setEbeyeXSDs(String ebeyeXSDs) {
        this.ebeyeXSDs = ebeyeXSDs;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getXmlDir() {
        return xmlDir;
    }

    public void setXmlDir(String xmlDir) {
        this.xmlDir = xmlDir;
    }
    
    

    @Override
    public String toString() {
        return "XmlConfigParams{" + "releaseNumber=" + releaseNumber + ", enzymeCentricXmlDir=" + enzymeCentricXmlDir + ", proteinCentricXmlDir=" + proteinCentricXmlDir + ", ebeyeXSDs=" + ebeyeXSDs + ", chunkSize=" + chunkSize + '}';
    }

}
