package uk.ac.ebi.ep.xml.util;

import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import static uk.ac.ebi.ep.xml.util.DateUtil.convertDateToString;
import static uk.ac.ebi.ep.xml.util.DateUtil.convertStringToDate;

/**
 * @author joseph
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String dateText) throws Exception {
        return convertStringToDate(dateText);
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return convertDateToString(date);
    }
}
