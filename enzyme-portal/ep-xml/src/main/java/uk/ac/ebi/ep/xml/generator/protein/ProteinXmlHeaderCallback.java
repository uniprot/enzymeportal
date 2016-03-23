package uk.ac.ebi.ep.xml.generator.protein;

import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import uk.ac.ebi.ep.xml.model.ModelDateUtil;

import com.google.common.base.Preconditions;
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

    final static String ENTRIES_ELEMENT = "entries";

    private final String release;
    private final EnzymePortalXmlService enzymePortalService;

    public ProteinXmlHeaderCallback(String release, EnzymePortalXmlService enzymePortalService) {
        Preconditions.checkArgument(enzymePortalService != null, "Enzyme portal XML service can not be null");
        Preconditions.checkArgument(release != null, "Release version can not be null");

        this.release = release;
        this.enzymePortalService = enzymePortalService;
    }

    @Override public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            appendTagWithValue(DB_NAME_ELEMENT, XmlTransformer.ENZYME_PORTAL, eventFactory, writer);
            appendTagWithValue(DB_DESCRIPTION_ELEMENT, XmlTransformer.ENZYME_PORTAL_DESCRIPTION, eventFactory, writer);
            appendTagWithValue(RELEASE_VERSION_ELEMENT, release, eventFactory, writer);
            appendTagWithValue(RELEASE_DATE_ELEMENT, createTodayDate(), eventFactory, writer);
            appendTagWithValue(ENTRY_COUNT_ELEMENT, queryNumberOfUniProtEntries(), eventFactory, writer);

            XMLEvent event = eventFactory.createStartElement("", "", ENTRIES_ELEMENT);
            writer.add(event);
        } catch (XMLStreamException ex) {
            throw new RuntimeException("Unable to write the header on the XML file");
        }
    }

    private void appendTagWithValue(String tag, String value, XMLEventFactory eventFactory, XMLEventWriter writer)
            throws XMLStreamException {
        writer.add(eventFactory.createStartElement("", "", tag));
        writer.add(eventFactory.createCharacters(value));
        writer.add(eventFactory.createEndElement("", "", tag));
    }

    private String createTodayDate() {
        return ModelDateUtil.convertDateToString(LocalDate.now());
    }

    private String queryNumberOfUniProtEntries() {
        String totalText = "0";

        Long total = enzymePortalService.countUniprotEntries();

        if(total != null) {
            totalText = String.valueOf(total);
        }

        return totalText;
    }
}