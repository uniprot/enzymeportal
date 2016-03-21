package uk.ac.ebi.ep.xml.generator.protein;

import uk.ac.ebi.ep.xml.generator.XmlTransformer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.springframework.batch.item.xml.StaxWriterCallback;

/**
 * Adds some meta data to the protein centric XML file:
 *    - Database name
 *    - Database description
 *    - Generation date
 *    - Total number of entries
 *
 * It also adds an entries tag so that the XML conforms to the Ebeye search format.
 *
 * @author Ricardo Antunes
 */
public class ProteinXmlHeaderCallback implements StaxWriterCallback {
    private final String release;

    public ProteinXmlHeaderCallback(String release) {
        this.release = release;
    }

    @Override public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            //TODO move names to somewhere else
            appendTag("name", XmlTransformer.enzymePortal, eventFactory, writer);
            appendTag("description", XmlTransformer.enzymePortalDescription, eventFactory, writer);
            appendTag("release", release, eventFactory, writer);
            appendTag("release_date", createTodayDate(), eventFactory, writer);
            appendTag("entry_count", "0", eventFactory, writer); //TODO calculate total number of entries

            XMLEvent event = eventFactory.createStartElement("", "", "entries");
            writer.add(event);

        } catch (XMLStreamException ex) {
            throw new RuntimeException("Unable to write the header on the XML file");
        }
    }

    private void appendTag(String tag, String name, XMLEventFactory eventFactory, XMLEventWriter writer)
            throws XMLStreamException {
        writer.add(eventFactory.createStartElement("", "", tag));
        writer.add(eventFactory.createCharacters(name));
        writer.add(eventFactory.createEndElement("", "", tag));
    }

    //TODO: find a way to reuse the date format defined in the data adaptor
    private String createTodayDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
    }
}