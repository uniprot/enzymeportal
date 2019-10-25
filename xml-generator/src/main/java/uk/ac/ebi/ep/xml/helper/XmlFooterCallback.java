package uk.ac.ebi.ep.xml.helper;

import java.io.IOException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.xml.StaxWriterCallback;
import uk.ac.ebi.ep.xml.util.EpXmlRuntimeException;

/**
 * Closes the entries element, that was added in the {@link XmlHeaderCallback}
 * class.
 *
 * @author Joseph
 */
@Slf4j
public class XmlFooterCallback implements StaxWriterCallback {

    @Override
    public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent event = eventFactory.createEndElement("", "", "entries");

            writer.add(event);
        } catch (XMLStreamException ex) {
            log.error("Unable to write the footer on the XML file", ex);
          
            throw new EpXmlRuntimeException("Unable to write the footer on the XML file", ex);
        }
    }

}
