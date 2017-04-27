
package uk.ac.ebi.ep.xml.generator.proteinGroup;

import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.generator.XmlTransformer;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 * @param <T> This source data class
 * @param <S> The entry to be written to XML
 */
public abstract class XmlProcessor<T,S> extends XmlTransformer implements ItemProcessor<T,S> {

    public XmlProcessor(XmlConfigParams xmlConfigParams) {
        super(xmlConfigParams);
    }

    @Override
    public abstract S process(T i) throws Exception;


}
