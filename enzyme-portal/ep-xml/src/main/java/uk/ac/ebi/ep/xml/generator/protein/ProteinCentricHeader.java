package uk.ac.ebi.ep.xml.generator.protein;

import com.google.common.base.Preconditions;
import java.io.IOException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import static uk.ac.ebi.ep.xml.generator.protein.ProteinXmlHeaderCallback.DB_NAME_ELEMENT;
import uk.ac.ebi.ep.xml.util.EpXmlRuntimeException;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinCentricHeader extends ProteinXmlHeaderCallback {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProteinCentricHeader.class);

    //private final String numEntries;

    public ProteinCentricHeader(String release, String numEntries, EnzymePortalXmlService xmlService) {
        //super(release, xmlService);
        super(release, numEntries);
        Preconditions.checkArgument(numEntries != null, "Number of Entries can not be null");

        //this.numEntries = numEntries;
    }

    @Override
    public void write(XMLEventWriter writer) throws IOException {
        try {
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            appendTagWithValue(DB_NAME_ELEMENT, XmlTransformer.ENZYME_PORTAL, eventFactory, writer);
            appendTagWithValue(DB_DESCRIPTION_ELEMENT, XmlTransformer.ENZYME_PORTAL_DESCRIPTION, eventFactory, writer);
            appendTagWithValue(RELEASE_VERSION_ELEMENT, release, eventFactory, writer);
            appendTagWithValue(RELEASE_DATE_ELEMENT, createTodayDate(), eventFactory, writer);
            appendTagWithValue(ENTRY_COUNT_ELEMENT, numEntries, eventFactory, writer);

            XMLEvent event = eventFactory.createStartElement("", "", ENTRIES_ELEMENT);
            writer.add(event);
        } catch (XMLStreamException ex) {
            logger.error("Unable to write the header on the XML file" + ex);
            throw new EpXmlRuntimeException("Unable to write the header on the XML file");
        }
    }
}
