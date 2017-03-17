
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Entity
@Table(name = "CHEBI_COMPOUND")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "ChebiCompound.findAll", query = "SELECT c FROM ChebiCompound c"),
//    @NamedQuery(name = "ChebiCompound.findByInternalId", query = "SELECT c FROM ChebiCompound c WHERE c.internalId = :internalId"),
//    //@NamedQuery(name = "ChebiCompound.findByStatus", query = "SELECT c FROM ChebiCompound c WHERE c.status = :status"),
//    //@NamedQuery(name = "ChebiCompound.findByChebiAccession", query = "SELECT c FROM ChebiCompound c WHERE c.chebiAccession = :chebiAccession"),
//    //@NamedQuery(name = "ChebiCompound.findBySource", query = "SELECT c FROM ChebiCompound c WHERE c.source = :source"),
//    //@NamedQuery(name = "ChebiCompound.findByCompoundName", query = "SELECT c FROM ChebiCompound c WHERE c.compoundName = :compoundName"),
//    @NamedQuery(name = "ChebiCompound.findByModifiedOn", query = "SELECT c FROM ChebiCompound c WHERE c.modifiedOn = :modifiedOn"),
//    @NamedQuery(name = "ChebiCompound.findByCreatedBy", query = "SELECT c FROM ChebiCompound c WHERE c.createdBy = :createdBy")
//    //@NamedQuery(name = "ChebiCompound.findByStar", query = "SELECT c FROM ChebiCompound c WHERE c.star = :star")
//})
public class ChebiCompound implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    //@Column(name = "STATUS")
    //private Character status;
    @Size(max = 30)
    @Column(name = "CHEBI_ACCESSION")
    private String chebiAccession;
    //@Size(max = 30)
   // @Column(name = "SOURCE")
   // private String source;
    @Size(max = 4000)
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
//    @Size(max = 10)
//    @Column(name = "MODIFIED_ON")
//    private String modifiedOn;
//    @Size(max = 15)
//    @Column(name = "CREATED_BY")
//    private String createdBy;
   // @Column(name = "STAR")
    //private BigInteger star;

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

//    public Character getStatus() {
//        return status;
//    }
//
//    public void setStatus(Character status) {
//        this.status = status;
//    }

    public String getChebiAccession() {
        return chebiAccession;
    }

    public void setChebiAccession(String chebiAccession) {
        this.chebiAccession = chebiAccession;
    }

//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

//    public String getModifiedOn() {
//        return modifiedOn;
//    }
//
//    public void setModifiedOn(String modifiedOn) {
//        this.modifiedOn = modifiedOn;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }

//    public BigInteger getStar() {
//        return star;
//    }
//
//    public void setStar(BigInteger star) {
//        this.star = star;
//    }

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
        return !((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.ChebiCompound[ internalId=" + internalId + " ]";
    }
    
}
