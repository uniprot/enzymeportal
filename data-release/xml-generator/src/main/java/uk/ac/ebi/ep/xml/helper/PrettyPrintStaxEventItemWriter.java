package uk.ac.ebi.ep.xml.helper;

import java.io.Writer;
import javanet.staxutils.IndentingXMLEventWriter;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import org.springframework.batch.item.xml.StaxEventItemWriter;

/**
 *
 * @author joseph
 * @param <T>
 */
public class PrettyPrintStaxEventItemWriter<T> extends StaxEventItemWriter<T> {

    private boolean indenting = true;

    @Override
    protected XMLEventWriter createXmlEventWriter(XMLOutputFactory outputFactory, Writer writer)
            throws XMLStreamException {

        if (indenting) {
            return new IndentingXMLEventWriter(super.createXmlEventWriter(outputFactory, writer));
        } else {
            return super.createXmlEventWriter(outputFactory, writer);
        }
    }

    public boolean isIndenting() {
        return indenting;
    }

    public void setIndenting(boolean indenting) {
        this.indenting = indenting;
    }


}
