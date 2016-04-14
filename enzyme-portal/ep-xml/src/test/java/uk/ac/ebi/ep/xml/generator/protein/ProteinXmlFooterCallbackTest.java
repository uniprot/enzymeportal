package uk.ac.ebi.ep.xml.generator.protein;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringEndsWith.endsWith;
import org.junit.Test;

/**
 * Tests the behaviour of the {@link ProteinXmlFooterCallback} class.
 */
public class ProteinXmlFooterCallbackTest extends AbstractCallbackSetup {
    private final ProteinXmlFooterCallback footerCallback = new ProteinXmlFooterCallback();

    @Test
    public void missingStartEntriesElementTagThrowsException() throws Exception {
        thrown.expectMessage("Unable to write the footer on the XML file");

        footerCallback.write(writer);
    }

    @Test
    public void addsClosingEntriesTagToOutput() throws Exception {
        addStartEntriesTag(writer);
        footerCallback.write(writer);

        assertThat(output.toString(), endsWith("</entries>"));
    }

    private void addStartEntriesTag(XMLEventWriter writer) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent openEntitiesTag = eventFactory.createStartElement("", "", "entries");
        writer.add(openEntitiesTag);
    }
}