package uk.ac.ebi.ep.xml.helper;

import java.io.IOException;
import java.time.LocalDate;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.xml.StaxWriterCallback;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;
import uk.ac.ebi.ep.xml.util.EpXmlRuntimeException;
import uk.ac.ebi.ep.xml.util.Preconditions;

/**
 * Adds some meta data to the XML file: - Database name - Database description -
 * Generation date - Total number of entries
 *
 * It also adds an entries tag so that the XML conforms to the Ebeye search
 * format.
 *
 * @author joseph<joseph@ebi.ac.uk>
 */
@Slf4j
public class XmlHeaderCallback implements StaxWriterCallback {


    //TODO move to a data class
    static final String DB_NAME_ELEMENT = "name";
    static final String DB_DESCRIPTION_ELEMENT = "description";
    static final String RELEASE_VERSION_ELEMENT = "release";
    static final String RELEASE_DATE_ELEMENT = "release_date";
    static final String ENTRY_COUNT_ELEMENT = "entry_count";

    static final String ENTRIES_ELEMENT = "entries";
    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";

    protected final String release;
    protected final String numEntries;

    public XmlHeaderCallback(String release, String numEntries) {
        Preconditions.checkArgument(numEntries == null, "Numberof entries can not be null");
        Preconditions.checkArgument(release == null, "Release version can not be null");

        this.release = release;
        this.numEntries = numEntries;
    }

    @Override
    public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            appendTagWithValue(DB_NAME_ELEMENT, ENZYME_PORTAL, eventFactory, writer);
            appendTagWithValue(DB_DESCRIPTION_ELEMENT, ENZYME_PORTAL_DESCRIPTION, eventFactory, writer);
            appendTagWithValue(RELEASE_VERSION_ELEMENT, release, eventFactory, writer);
            appendTagWithValue(RELEASE_DATE_ELEMENT, createTodayDate(), eventFactory, writer);
            appendTagWithValue(ENTRY_COUNT_ELEMENT, numEntries, eventFactory, writer);

            XMLEvent event = eventFactory.createStartElement("", "", ENTRIES_ELEMENT);

            writer.add(event);
        } catch (XMLStreamException ex) {
            log.error("Unable to write the header on the XML file" + ex);
            throw new EpXmlRuntimeException("Unable to write the header on the XML file");
        }
    }

    protected void appendTagWithValue(String tag, String value, XMLEventFactory eventFactory, XMLEventWriter writer)
            throws XMLStreamException {
        writer.add(eventFactory.createStartElement("", "", tag));
        writer.add(eventFactory.createCharacters(value));
        writer.add(eventFactory.createEndElement("", "", tag));
    }

    protected String createTodayDate() {
        return DateTimeUtil.convertDateToString(LocalDate.now());
    }
    
    

}
