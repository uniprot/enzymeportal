package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "UNIPROT_XREF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniprotXref.findAll", query = "SELECT u FROM UniprotXref u"),
    @NamedQuery(name = "UniprotXref.findByXrefId", query = "SELECT u FROM UniprotXref u WHERE u.xrefId = :xrefId"),
    @NamedQuery(name = "UniprotXref.findByDbentryId", query = "SELECT u FROM UniprotXref u WHERE u.dbentryId = :dbentryId"),
    @NamedQuery(name = "UniprotXref.findBySourceId", query = "SELECT u FROM UniprotXref u WHERE u.sourceId = :sourceId"),
    @NamedQuery(name = "UniprotXref.findBySource", query = "SELECT u FROM UniprotXref u WHERE u.source = :source")})
public class UniprotXref implements Comparable<UniprotXref>, Serializable {
     private static final long serialVersionUID = 1L;
    @Column(name = "SOURCE_NAME")
    private String sourceName;
   
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "XREF_ID")
    private BigDecimal xrefId;
    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Column(name = "SOURCE_ID")
    private String sourceId;
    @Column(name = "SOURCE")
    private String source;
    @JoinColumn(name = "ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry accession;

    public UniprotXref() {
    }

    public UniprotXref(BigDecimal xrefId) {
        this.xrefId = xrefId;
    }

    public UniprotXref(BigDecimal xrefId, long dbentryId) {
        this.xrefId = xrefId;
        this.dbentryId = dbentryId;
    }

    public BigDecimal getXrefId() {
        return xrefId;
    }

    public void setXrefId(BigDecimal xrefId) {
        this.xrefId = xrefId;
    }

    public long getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(long dbentryId) {
        this.dbentryId = dbentryId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UniprotEntry getAccession() {
        return accession;
    }

    public void setAccession(UniprotEntry accession) {
        this.accession = accession;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.xrefId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UniprotXref other = (UniprotXref) obj;
        if (!Objects.equals(this.xrefId, other.xrefId)) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "UniprotXref{" + "sourceName=" + sourceName + ", sourceId=" + sourceId + ", source=" + source + '}';
    }



    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public int compareTo(UniprotXref p) {
       return this.sourceId.compareToIgnoreCase(p.getSourceId());
    }
    
}
