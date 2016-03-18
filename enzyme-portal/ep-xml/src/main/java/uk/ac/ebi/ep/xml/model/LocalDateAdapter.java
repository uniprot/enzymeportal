
package uk.ac.ebi.ep.xml.model;

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
        return date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu"));
    }

}
