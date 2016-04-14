package uk.ac.ebi.ep.xml.generator.protein;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;

/**
 * Abstract class containing common methods used by the implementing classes.
 */
public abstract class AbstractCallbackSetup {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected StringWriter output;
    protected XMLEventWriter writer;

    @Before
    public void setUp() throws Exception {
        output = new StringWriter();
        writer = setupEventWriter(output);
    }

    private XMLEventWriter setupEventWriter(StringWriter output) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        return factory.createXMLEventWriter(output);
    }

    @After
    public void tearDown() throws Exception {
        writer.close();
        output.close();
    }

    protected Document parse(String xml) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setValidating(false);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
    }
}
