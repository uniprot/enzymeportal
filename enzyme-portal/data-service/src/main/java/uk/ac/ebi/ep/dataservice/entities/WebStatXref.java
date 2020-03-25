package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "WEB_STAT_XREF")
public class WebStatXref implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "webStatXrefSeqGenerator", sequenceName = "seq_web_stat_xref")
    @GeneratedValue(generator = "webStatXrefSeqGenerator", strategy = GenerationType.SEQUENCE)
    private Long internalId;
    @Column(name = "UNIPROT")
    private long uniprot;
    @Column(name = "INTENZ")
    private long intenz;
    @Column(name = "REACTOME")
    private long reactome;
    @Column(name = "RHEA")
    private long rhea;
    @Column(name = "KEGG")
    private long kegg;
    @Column(name = "PDBE")
    private long pdbe;
    @Column(name = "CHEMBL")
    private long chembl;
    @Column(name = "CHEBI")
    private long chebi;
    @Column(name = "OMIM")
    private long omim;
    @Column(name = "METABOLIGHTS")
    private long metabolights;
    @Column(name = "MCSA")
    private long mcsa;
    @Column(name = "EUROPEPMC")
    private long europepmc;
    @Column(name = "BRENDA")
    private long brenda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "RELEASE_ID")
    private String releaseId;
    @Column(name = "RELEASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
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
