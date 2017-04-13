/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
