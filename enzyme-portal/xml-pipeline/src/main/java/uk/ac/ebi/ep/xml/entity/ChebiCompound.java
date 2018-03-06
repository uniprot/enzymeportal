/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "CHEBI_COMPOUND")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChebiCompound.findAll", query = "SELECT c FROM ChebiCompound c"),
    @NamedQuery(name = "ChebiCompound.findByInternalId", query = "SELECT c FROM ChebiCompound c WHERE c.internalId = :internalId"),
    @NamedQuery(name = "ChebiCompound.findByChebiAccession", query = "SELECT c FROM ChebiCompound c WHERE c.chebiAccession = :chebiAccession"),
    @NamedQuery(name = "ChebiCompound.findByCompoundName", query = "SELECT c FROM ChebiCompound c WHERE c.compoundName = :compoundName"),
    @NamedQuery(name = "ChebiCompound.findBySource", query = "SELECT c FROM ChebiCompound c WHERE c.source = :source")})
public class ChebiCompound implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    @Column(name = "CHEBI_ACCESSION")
    private String chebiAccession;
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Column(name = "SOURCE")
    private String source;

    public ChebiCompound() {
    }

    public ChebiCompound(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public BigDecimal getInternalId() {
        return internalId;
    }

    public void setInternalId(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public String getChebiAccession() {
        return chebiAccession;
    }

    public void setChebiAccession(String chebiAccession) {
        this.chebiAccession = chebiAccession;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChebiCompound)) {
            return false;
        }
        ChebiCompound other = (ChebiCompound) object;
        if ((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.xmlpipeline.model.ChebiCompound[ internalId=" + internalId + " ]";
    }
    
}
