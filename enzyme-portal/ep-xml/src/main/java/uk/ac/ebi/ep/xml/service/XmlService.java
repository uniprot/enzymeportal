/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.service;

import javax.xml.bind.JAXBException;
import uk.ac.ebi.ep.xml.util.Preconditions;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 * Service to generate both protein and enzyme centric XML.
 * <h2>usage</h2>
 * <pre><code>  XmlService service = applicationContext.getBean(EnzymeCentric|Protein.class);</code></pre>
 *
 * @author Joseph <joseph@ebi.ac.uk>
 *
 */
public interface XmlService {

    /**
     * validate an XMLfile against XSD file(s)
     *
     * @param xmlFile the XML file
     * @param xsdFiles the XSD file(s)
     * @return true if validated otherwise false.
     */
    default boolean validateXML(final String xmlFile, final String[] xsdFiles) {
        Preconditions.checkArgument(xsdFiles == null, "XSD file to be validated against cannot be null. Please ensure that ep-xml-config.properties is in the classpath.");
        Preconditions.checkArgument(xmlFile == null, "At least an XML File must be provided for XML validation to proceed.");

        return EnzymePortalXmlValidator.validateXml(xmlFile, xsdFiles);
    }

    /**
     * Generate the xml in a specified location. location needs to be a
     * dir/filename.xml
     *
     * @param fileLocation location for the generated xml
     * @throws JAXBException
     */
    void generateXmL(String fileLocation) throws JAXBException;
}
