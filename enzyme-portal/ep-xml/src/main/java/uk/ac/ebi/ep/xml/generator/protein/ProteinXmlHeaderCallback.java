package uk.ac.ebi.ep.xml.generator.protein;

import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import uk.ac.ebi.ep.xml.model.ModelDateUtil;

import java.io.IOException;
import java.time.LocalDate;
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
    final static String DB_NAME_ELEMENT = "name";
    final static String DB_DESCRIPTION_ELEMENT = "description";
    final static String RELEASE_VERSION_ELEMENT = "release";
    final static String RELEASE_DATE_ELEMENT = "release_date";
    final static String ENTRY_COUNT_ELEMENT = "entry_count";

    private final String release;

    public ProteinXmlHeaderCallback(String release) {
        this.release = release;
    }

    @Override public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            //TODO move names to somewhere else
            appendTag(DB_NAME_ELEMENT, XmlTransformer.enzymePortal, eventFactory, writer);
            appendTag(DB_DESCRIPTION_ELEMENT, XmlTransformer.enzymePortalDescription, eventFactory, writer);
            appendTag(RELEASE_VERSION_ELEMENT, release, eventFactory, writer);
            appendTag(RELEASE_DATE_ELEMENT, createTodayDate(), eventFactory, writer);
            appendTag(ENTRY_COUNT_ELEMENT, "0", eventFactory, writer); //TODO calculate total number of entries

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

    private String createTodayDate() {
        return ModelDateUtil.convertDateToString(LocalDate.now());
    }
}