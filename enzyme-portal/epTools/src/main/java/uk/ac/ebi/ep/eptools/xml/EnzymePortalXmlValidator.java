/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.eptools.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * @param xmlFilePathAndName Path/name of XML file to be validated; should
     * not be null or empty.
     * @param xsdFilesPathsAndNames XSDs against which to validate the XML;
     * should not be null or empty.
     */
    public static void validateXmlAgainstXsds(
            final String xmlFilePathAndName, final String[] xsdFilesPathsAndNames) {
        if (xmlFilePathAndName == null || xmlFilePathAndName.isEmpty()) {
            logger.error("ERROR: Path/name of XML to be validated cannot be null.");
            return;
        }
        if (xsdFilesPathsAndNames == null || xsdFilesPathsAndNames.length < 1) {
            logger.error("ERROR: At least one XSD must be provided to validate XML against.");
            return;
        }
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final StreamSource[] xsdSources = generateStreamSourcesFromXsdPathsJdk8(xsdFilesPathsAndNames);

        try {
            final Schema schema = schemaFactory.newSchema(xsdSources);
            final Validator validator = schema.newValidator();
            String msg = "Validating " + xmlFilePathAndName + " against XSDs "
                    + Arrays.toString(xsdFilesPathsAndNames) + "...";

            logger.warn(msg);
            validator.validate(new StreamSource(new File(xmlFilePathAndName)));
        } catch (IOException | SAXException exception) 
        {

            String error = ("ERROR: Unable to validate " + xmlFilePathAndName
                    + " against XSDs " + Arrays.toString(xsdFilesPathsAndNames)
                    + " - " + exception);

            logger.error(error);
        }

        logger.warn("XML Validation process is now completed.");
    }

    /**
     * Generates array of StreamSource instances representing XSDs associated
     * with the file paths/names provided and use JDK 8 Stream API.
     *
     * This method can be commented out if using a version of Java prior to JDK
     * 8.
     *
     * @param xsdFilesPaths String representations of paths/names of XSD files.
     * @return StreamSource instances representing XSDs.
     */
    private static StreamSource[] generateStreamSourcesFromXsdPathsJdk8(
            final String[] xsdFilesPaths) {
        return Arrays.stream(xsdFilesPaths)
                .map(StreamSource::new)
                .collect(Collectors.toList())
                .toArray(new StreamSource[xsdFilesPaths.length]);
    }

    /**
     * Generates array of StreamSource instances representing XSDs associated
     * with the file paths/names provided and uses pre-JDK 8 Java APIs.
     *
     * This method can be commented out (or better yet, removed altogether) if
     * using JDK 8 or later.
     *
     * @param xsdFilesPaths String representations of paths/names of XSD files.
     * @return StreamSource instances representing XSDs.
     * @deprecated Use generateStreamSourcesFromXsdPathsJdk8 instead when JDK 8
     * or later is available.
     */
    @Deprecated
    private static StreamSource[] generateStreamSourcesFromXsdPathsJdk7(
            final String[] xsdFilesPaths) {
        // Diamond operator used here requires JDK 7; add type of
        // StreamSource to generic specification of ArrayList for
        // JDK 5 or JDK 6
        final List<StreamSource> streamSources = new ArrayList<>();
        for (final String xsdPath : xsdFilesPaths) {
            streamSources.add(new StreamSource(xsdPath));
        }
        return streamSources.toArray(new StreamSource[xsdFilesPaths.length]);
    }

}
