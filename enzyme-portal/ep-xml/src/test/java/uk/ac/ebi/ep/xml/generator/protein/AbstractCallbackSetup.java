package uk.ac.ebi.ep.xml.generator.protein;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Abstract class containing common methods used by the implementing classes.
 */
public abstract class AbstractCallbackSetup {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected StringWriter output;
    protected XMLEventWriter writer;

    @Before
    public void setUp() throws XMLStreamException  {
        output = new StringWriter();
        writer = setupEventWriter(output);
    }

    private XMLEventWriter setupEventWriter(StringWriter output) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        return factory.createXMLEventWriter(output);
    }

    @After
    public void tearDown() throws XMLStreamException, IOException  {
        writer.close();
        output.close();
    }

    protected Document parse(String xml) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setValidating(false);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
    }
}
