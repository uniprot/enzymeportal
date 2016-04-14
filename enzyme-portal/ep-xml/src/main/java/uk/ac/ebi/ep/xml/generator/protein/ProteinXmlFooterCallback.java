package uk.ac.ebi.ep.xml.generator.protein;

import java.io.IOException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.springframework.batch.item.xml.StaxWriterCallback;

/**
 * Closes the entries element, that was added in the {@link ProteinXmlHeaderCallback} class.
 *
 * @author Ricardo Antunes
 */
public class ProteinXmlFooterCallback implements StaxWriterCallback {
    @Override public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent event = eventFactory.createEndElement("", "", "entries");
            writer.add(event);
        } catch (XMLStreamException ex) {
            throw new RuntimeException("Unable to write the footer on the XML file", ex);
        }
    }
}
