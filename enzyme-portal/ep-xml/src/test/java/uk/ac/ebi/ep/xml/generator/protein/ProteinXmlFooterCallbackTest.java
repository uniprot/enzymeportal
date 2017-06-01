package uk.ac.ebi.ep.xml.generator.protein;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringEndsWith.endsWith;
import org.junit.Test;
import uk.ac.ebi.ep.xml.util.EpXmlRuntimeException;

/**
 * Tests the behaviour of the {@link ProteinXmlFooterCallback} class.
 */
public class ProteinXmlFooterCallbackTest extends AbstractCallbackSetup {

    private final ProteinXmlFooterCallback footerCallback = new ProteinXmlFooterCallback();

    @Test(expected = EpXmlRuntimeException.class)
    public void missingStartEntriesElementTagThrowsException() {
        //thrown.expectMessage("Unable to write the footer on the XML file");

        try {
            footerCallback.write(writer);
        } catch (IOException ex) {
            Logger.getLogger(ProteinXmlFooterCallbackTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void addsClosingEntriesTagToOutput() throws XMLStreamException, IOException {
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
