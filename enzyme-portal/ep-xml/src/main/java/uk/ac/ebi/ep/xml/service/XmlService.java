/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.service;

import javax.xml.bind.JAXBException;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**Service to generate both protein and enzyme centric XML.
 * <h2>usage</h2>
 * <pre><code>  XmlService service = applicationContext.getBean(EnzymeCentric|Protein.class);</code></pre>
 *
 * @author Joseph <joseph@ebi.ac.uk>
 *
 */
public interface XmlService {

    /**
     * uses default directories & XSDs provided by the implementing class
     */
    void validateXML();

    /**
     * validate an XMLfile against XSD file(s)
     *
     * @param xmlFile the XML file
     * @param xsdFiles the XSD file(s)
     */
    default void validateXML(final String xmlFile, final String[] xsdFiles) {

        EnzymePortalXmlValidator.validateXml(xmlFile, xsdFiles);
    }

    void generateXmL() throws JAXBException;
}
