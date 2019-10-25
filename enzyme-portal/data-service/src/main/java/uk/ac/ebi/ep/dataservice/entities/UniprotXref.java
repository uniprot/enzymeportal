package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "UNIPROT_XREF")
@XmlRootElement
@NamedQuery(name = "UniprotXref.findAll", query = "SELECT u FROM UniprotXref u")
@NamedQuery(name = "UniprotXref.findByXrefId", query = "SELECT u FROM UniprotXref u WHERE u.xrefId = :xrefId")
@NamedQuery(name = "UniprotXref.findByDbentryId", query = "SELECT u FROM UniprotXref u WHERE u.dbentryId = :dbentryId")
@NamedQuery(name = "UniprotXref.findBySourceId", query = "SELECT u FROM UniprotXref u WHERE u.sourceId = :sourceId")
@NamedQuery(name = "UniprotXref.findBySource", query = "SELECT u FROM UniprotXref u WHERE u.source = :source")
@NamedQuery(name = "UniprotXref.findBySourceName", query = "SELECT u FROM UniprotXref u WHERE u.sourceName = :sourceName")
public class UniprotXref implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "XREF_ID")
    private BigDecimal xrefId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Size(max = 60)
    @Column(name = "SOURCE_ID")
    private String sourceId;
    @Size(max = 8)
    @Column(name = "SOURCE")
    private String source;
    @Size(max = 4000)
    @Column(name = "SOURCE_NAME")
    private String sourceName;
    @JoinColumn(name = "ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false)
    private UniprotEntry accession;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.xrefId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UniprotXref other = (UniprotXref) obj;
        return Objects.equals(this.xrefId, other.xrefId);
    }

}
