/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class UniprotXref implements Serializable {
    private static final long serialVersionUID = 1L;
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
        int hash = 0;
        hash += (xrefId != null ? xrefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniprotXref)) {
            return false;
        }
        UniprotXref other = (UniprotXref) object;
        if ((this.xrefId == null && other.xrefId != null) || (this.xrefId != null && !this.xrefId.equals(other.xrefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.UniprotXref[ xrefId=" + xrefId + " ]";
    }
    
}
