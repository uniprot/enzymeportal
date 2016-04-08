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
import uk.ac.ebi.ep.xml.util.Preconditions;

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
     * @return true if validated otherwise false
     */
    public static boolean validateXml(final String xmlFile, final String[] xsdFiles) {
        Preconditions.checkArgument(xmlFile == null || xmlFile.isEmpty(), "XML file to be validated cannot be null.");
        Preconditions.checkArgument(xsdFiles == null || xsdFiles.length < 1, "At least an XSD File must be provided for XML validation to proceed.");

        File file = new File(xmlFile);
        Preconditions.checkArgument(file.exists() == false, "XML file does not exist.");

        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final StreamSource[] streamSources = streamSourcesFromXsdFiles(xsdFiles);

        try {
            final Schema schema = schemaFactory.newSchema(streamSources);
            final Validator validator = schema.newValidator();
            String info = "Started validating " + xmlFile + " against XSDs " + Arrays.toString(xsdFiles) + "...";

            logger.info(info);
            validator.validate(new StreamSource(new File(xmlFile)));
            logger.warn("The validation of the XML file in this dir [" + xmlFile + "] seems successful.");
        } catch (IOException | SAXException exception) {

            String errorMsg = ("ERROR: Unable to validate " + xmlFile + " against XSDs " + Arrays.toString(xsdFiles) + " - " + exception);

            logger.error(errorMsg);
            return false;
        }

        logger.info("XML Validation process is now completed.Please check the logs for more info.");
        return true;
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
