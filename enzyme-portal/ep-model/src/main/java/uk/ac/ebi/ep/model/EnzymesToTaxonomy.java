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
@Table(name = "ENZYMES_TO_TAXONOMY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymesToTaxonomy.findAll", query = "SELECT e FROM EnzymesToTaxonomy e"),
    @NamedQuery(name = "EnzymesToTaxonomy.findByInternalId", query = "SELECT e FROM EnzymesToTaxonomy e WHERE e.internalId = :internalId"),
    @NamedQuery(name = "EnzymesToTaxonomy.findByEcNumber", query = "SELECT e FROM EnzymesToTaxonomy e WHERE e.ecNumber = :ecNumber"),
    @NamedQuery(name = "EnzymesToTaxonomy.findByTaxId", query = "SELECT e FROM EnzymesToTaxonomy e WHERE e.taxId = :taxId"),
    @NamedQuery(name = "EnzymesToTaxonomy.findByScientificName", query = "SELECT e FROM EnzymesToTaxonomy e WHERE e.scientificName = :scientificName"),
    @NamedQuery(name = "EnzymesToTaxonomy.findByCommonName", query = "SELECT e FROM EnzymesToTaxonomy e WHERE e.commonName = :commonName")})
public class EnzymesToTaxonomy implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    @Size(max = 15)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Column(name = "TAX_ID")
    private BigInteger taxId;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;

    public EnzymesToTaxonomy() {
    }

    public EnzymesToTaxonomy(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public BigDecimal getInternalId() {
        return internalId;
    }

    public void setInternalId(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public BigInteger getTaxId() {
        return taxId;
    }

    public void setTaxId(BigInteger taxId) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymesToTaxonomy)) {
            return false;
        }
        EnzymesToTaxonomy other = (EnzymesToTaxonomy) object;
        if ((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.ep.model.EnzymesToTaxonomy[ internalId=" + internalId + " ]";
    }
    
}
