package uk.ac.ebi.ep.xml.helper;

import java.io.Writer;
import javanet.staxutils.IndentingXMLEventWriter;
import javanet.staxutils.helpers.EventWriterDelegate;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import org.springframework.batch.item.xml.StaxEventItemWriter;

/**
 * Extension of the {@link StaxEventItemWriter} that indents the xml tags.
 *
 * @author Joseph
 */
public class CustomStaxEventItemWriter<T> extends StaxEventItemWriter<T> {
    @Override
    protected XMLEventWriter createXmlEventWriter(XMLOutputFactory outputFactory, Writer writer)
            throws XMLStreamException {
        XMLEventWriter eventWriter = super.createXmlEventWriter(outputFactory, writer);
        IndentingXMLEventWriter indentingXMLEventWriter = new IndentingXMLEventWriter(eventWriter);
        return new XMLEventWriterFilteringBlankCharacterEvents(indentingXMLEventWriter);
    }

    @Override
    protected void endDocument(XMLEventWriter writer) throws XMLStreamException {
        XMLEventFactory factory = createXmlEventFactory();
        writer.add(factory.createEndElement(getRootTagNamespacePrefix(), getRootTagNamespace(), getRootTagName()));
        writer.add(factory.createEndDocument());
    }

    private static class XMLEventWriterFilteringBlankCharacterEvents extends EventWriterDelegate {

        public XMLEventWriterFilteringBlankCharacterEvents(XMLEventWriter out) {
            super(out);
        }

        @Override
        public void add(XMLEvent event) throws XMLStreamException {
            if (event.getEventType() == XMLStreamConstants.CHARACTERS) {
                Characters characters = event.asCharacters();
                if (characters.getData().isEmpty()) {
//                    LOGGER.debug("Skipping blank event characters, {}", event);

                    return;
                }
            }
            super.add(event);         
        }
    }
}