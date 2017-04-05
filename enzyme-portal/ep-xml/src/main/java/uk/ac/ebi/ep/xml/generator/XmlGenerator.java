package uk.ac.ebi.ep.xml.generator;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.service.XmlService;
import uk.ac.ebi.ep.xml.util.Preconditions;
import uk.ac.ebi.ep.xml.util.XmlFileUtils;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public abstract class XmlGenerator extends XmlTransformer implements XmlService {

    protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(XmlGenerator.class);

    protected EnzymePortalXmlService enzymePortalXmlService;

    public XmlGenerator(EnzymePortalXmlService enzymePortalXmlService, XmlConfigParams xmlConfigParams) {
        super(xmlConfigParams);
        Preconditions.checkArgument(enzymePortalXmlService == null, "enzymePortalXmlService can not be null");
        this.enzymePortalXmlService = enzymePortalXmlService;
    }

    /**
     * generate the XML in the default location declared in the config files
     *
     * @throws JAXBException
     */
    public abstract void generateXmL() throws JAXBException;

    /**
     * uses default directories & XSDs provided by the implementing class.
     * implementing class must provide XML file location and XSD files to
     * validate against
     */
    public abstract void validateXML();

    /**
     * uses default XSDs provided to validate the generated XML file
     *
     * @param xmlFile xml dir/file
     * @param ebeyeXSDs XSD file to validate against
     * @return true if validated otherwise false
     */
    public boolean validateXML(String xmlFile, String ebeyeXSDs) {

        Preconditions.checkArgument(ebeyeXSDs == null, "XSD file to be validated against cannot be null. Please ensure that ep-xml-config.properties is in the classpath.");
        Preconditions.checkArgument(xmlFile == null, "At least an XML File must be provided for XML validation to proceed.");
        String[] xsdFiles = ebeyeXSDs.split(",");

        return EnzymePortalXmlValidator.validateXml(xmlFile, xsdFiles);

    }

    protected <U> void writeXml(U data, String xmlDir, String xmlFile) throws JAXBException {
        // create JAXB context and instantiate marshaller
        Marshaller m = xmlMarshaller(data.getClass());
        try {
            Writer writer = createPathToWriteXML(xmlDir, xmlFile);
            //m.marshal(data, System.out);
            // Write to File
            m.marshal(data, writer);
            //m.marshal(data, new File(enzymeCentricXmlDir));

            logger.info("Done writing XML to this Location :" + xmlFile);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    protected Writer createPathToWriteXML(String xmlDir, String xmlFile) throws IOException {
        Path path = Paths.get(xmlFile);
        boolean isFileExists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        if (isFileExists == false) {
            XmlFileUtils.createDirectory(xmlDir);
        }
        Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        return writer;
    }

    protected <T> Marshaller xmlMarshaller(Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }

    protected <T> org.springframework.oxm.Marshaller springXmlMarshaller(Class<T> clazz) throws JAXBException {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(clazz);

        Map<String, Object> jaxbProps = new HashMap<>();
        jaxbProps.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.setMarshallerProperties(jaxbProps);

        return marshaller;
    }

    }
