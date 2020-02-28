package uk.ac.ebi.ep.model.dao;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author joseph
 */
public interface WebStatXrefView {

    long getUniprot();

    long getIntenz();

    long getOmim();

    long getPdb();

    long getMetabolights();

    long getKegg();

    long getRhea();

    long getReactome();

    long getChembl();

    long getChebi();

    String getReleaseId();

    Date getReleaseDate();

    default String getMonthShortName() {
 
        int monthInNumber = Integer.parseInt(getReleaseId().split("-")[0]);

        Month month = Month.of(monthInNumber);

        return month.getDisplayName(TextStyle.SHORT, Locale.UK);

    }

    default String getMonthFullName() {
 
        int monthInNumber = Integer.parseInt(getReleaseId().split("-")[0]);

        Month month = Month.of(monthInNumber);

        return month.getDisplayName(TextStyle.FULL, Locale.UK);

    }

    default String getYear() {
   
        return getReleaseId().split("-")[1];
    }

}
