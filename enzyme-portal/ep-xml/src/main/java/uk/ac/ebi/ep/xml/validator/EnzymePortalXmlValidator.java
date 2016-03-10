/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.validator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Validates an XML against a supplied XSDs.
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymePortalXmlValidator {

    private static final Logger logger = Logger.getLogger(EnzymePortalXmlValidator.class);

    private EnzymePortalXmlValidator() {

    }
    

    /**
     * Validate provided XML against the provided XSD schema files.
     *
     * @param xmlFile XML file to be validated; should not be null or empty.
     * @param xsdFiles XSDs against which to validate the XML should not be null
     * or empty.
     */
    public static void validateXml( final String xmlFile, final String[] xsdFiles) {
        
        if (xmlFile == null || xmlFile.isEmpty()) {
            logger.error("ERROR: XML file to be validated cannot be null.");
            return;
        }
        if (xsdFiles == null || xsdFiles.length < 1) {
            logger.error("ERROR: At least an XSD File must be provided for XML validation to proceed.");
            return;
        }
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final StreamSource[] streamSources = streamSourcesFromXsdFiles(xsdFiles);

        try {
            final Schema schema = schemaFactory.newSchema(streamSources);
            final Validator validator = schema.newValidator();
            String info = "Started validating " + xmlFile + " against XSDs " + Arrays.toString(xsdFiles) + "...";

            logger.info(info);
            validator.validate(new StreamSource(new File(xmlFile)));
        } catch (IOException | SAXException exception) {

            String errorMsg = ("ERROR: Unable to validate " + xmlFile + " against XSDs " + Arrays.toString(xsdFiles) + " - " + exception);

            logger.error(errorMsg);
        }

        logger.info("XML Validation process is now completed.Please check the logs for more info.");
        
    }

    /**
     * Generates an array of StreamSource instances for the XSD file provided
     *
     *
     *
     * @param xsdFiles XSD files.
     * @return StreamSource instances of XSDs.
     */
    private static StreamSource[] streamSourcesFromXsdFiles(final String... xsdFiles) {
        return Arrays.stream(xsdFiles)
                .map(StreamSource::new)
                .collect(Collectors.toList())
                .toArray(new StreamSource[xsdFiles.length]);
    }

}
