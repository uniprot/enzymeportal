/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.converters;

import org.springframework.core.convert.converter.Converter;
import uk.ac.ebi.ep.download.EnzymeCentric;

/**
 *
 * @author Joseph
 */
public class StringToEnzymeCentric implements Converter<String, EnzymeCentric> {

    @Override
    public EnzymeCentric convert(String s) {

//        return EnzymeCentric.enzymeBuilder().ec(s).build();
        return new EnzymeCentric(s);
    }

}
