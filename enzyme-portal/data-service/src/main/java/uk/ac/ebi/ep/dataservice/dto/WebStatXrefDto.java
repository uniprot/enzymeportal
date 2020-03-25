package uk.ac.ebi.ep.dataservice.dto;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import lombok.Value;
import org.springframework.data.web.ProjectedPayload;

/**
 *
 * @author joseph
 */
@ProjectedPayload
@Value
public class WebStatXrefDto {

    private long uniprot;

    private long intenz;

    private long reactome;

    private long rhea;

    private long kegg;

    private long pdbe;

    private long chembl;

    private long chebi;

    private long omim;

    private long metabolights;

    private long mcsa;

    private long europepmc;

    private long brenda;

    private String releaseId;

    private Date releaseDate;
    
    

    public String getMonthShortName() {

        int monthInNumber = Integer.parseInt(getReleaseId().split("-")[0]);

        Month month = Month.of(monthInNumber);

        return month.getDisplayName(TextStyle.SHORT, Locale.UK);

    }

    public String getMonthFullName() {

        int monthInNumber = Integer.parseInt(getReleaseId().split("-")[0]);

        Month month = Month.of(monthInNumber);

        return month.getDisplayName(TextStyle.FULL, Locale.UK);

    }

    public String getYear() {

        return getReleaseId().split("-")[1];
    }

}
