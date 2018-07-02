/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry;

/**
 *
 * @author Joseph
 */
public class StringToEnzymeEntry implements Converter<String, List<uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry>> {

    // public class StringToEnzymeEntry implements Converter<String, uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry> {
    private final List<EnzymeEntry> entries = new ArrayList<>();

//    public StringToEnzymeEntry() {
//        this.entries = new ArrayList<>();
//    }
    @Override
    public List<EnzymeEntry> convert(String s) {
        EnzymeEntry entry = new EnzymeEntry();
        entry.setId(s);
        return Stream.<EnzymeEntry>builder().add(entry).build().collect(Collectors.toList());
        //return Lists.newArrayList(new EnzymeEntry(s));
//        List<EnzymeEntry> entries = new ArrayList<>();
//        entries.add(new EnzymeEntry(s));
//        return entries;
    }

//    @Override
//    public EnzymeEntry convert(String s) {
//       
//        //return new EnzymeEntry(s);
//       EnzymeEntry entry = new EnzymeEntry();
//       entry.setId(s);
//       return entry;
//    }
}
