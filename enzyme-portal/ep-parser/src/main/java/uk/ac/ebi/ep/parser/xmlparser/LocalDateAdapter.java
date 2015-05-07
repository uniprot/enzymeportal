/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.xmlparser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author joseph
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);

    }

    @Override
    public String marshal(LocalDate date) throws Exception {
//return date.toString();
        //return date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
         return date.format(DateTimeFormatter.ofPattern("uuuu-MMM-d"));
    }

}
