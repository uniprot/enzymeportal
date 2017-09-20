/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "ORGANISM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organism.findAll", query = "SELECT o FROM Organism o"),
    @NamedQuery(name = "Organism.findByTaxId", query = "SELECT o FROM Organism o WHERE o.taxId = :taxId"),
    @NamedQuery(name = "Organism.findByScientificName", query = "SELECT o FROM Organism o WHERE o.scientificName = :scientificName"),
    @NamedQuery(name = "Organism.findByCommonName", query = "SELECT o FROM Organism o WHERE o.commonName = :commonName"),
    @NamedQuery(name = "Organism.findByExpEvidenceFlag", query = "SELECT o FROM Organism o WHERE o.expEvidenceFlag = :expEvidenceFlag"),
    @NamedQuery(name = "Organism.findByRanking", query = "SELECT o FROM Organism o WHERE o.ranking = :ranking")})
public class Organism implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TAX_ID")
    private BigDecimal taxId;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;
    @Column(name = "RANKING")
    private BigInteger ranking;

    public Organism() {
    }

    public Organism(BigDecimal taxId) {
        this.taxId = taxId;
    }

    public BigDecimal getTaxId() {
        return taxId;
    }

    public void setTaxId(BigDecimal taxId) {
        this.taxId = taxId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public BigInteger getExpEvidenceFlag() {
        return expEvidenceFlag;
    }

    public void setExpEvidenceFlag(BigInteger expEvidenceFlag) {
        this.expEvidenceFlag = expEvidenceFlag;
    }

    public BigInteger getRanking() {
        return ranking;
    }

    public void setRanking(BigInteger ranking) {
        this.ranking = ranking;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxId != null ? taxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organism)) {
            return false;
        }
        Organism other = (Organism) object;
        if ((this.taxId == null && other.taxId != null) || (this.taxId != null && !this.taxId.equals(other.taxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.ep.model.Organism[ taxId=" + taxId + " ]";
    }
    
}
