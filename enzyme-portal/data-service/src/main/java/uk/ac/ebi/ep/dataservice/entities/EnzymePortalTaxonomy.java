/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
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
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_TAXONOMY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalTaxonomy.findAll", query = "SELECT e FROM EnzymePortalTaxonomy e"),
    @NamedQuery(name = "EnzymePortalTaxonomy.findByTaxId", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.taxId = :taxId"),
    @NamedQuery(name = "EnzymePortalTaxonomy.findByScientificName", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.scientificName = :scientificName"),
    @NamedQuery(name = "EnzymePortalTaxonomy.findByCommonName", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.commonName = :commonName"),
    @NamedQuery(name = "EnzymePortalTaxonomy.findByModelOrganism", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.modelOrganism = :modelOrganism"),
    @NamedQuery(name = "EnzymePortalTaxonomy.findByNumberOfProteins", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.numberOfProteins = :numberOfProteins")})
public class EnzymePortalTaxonomy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TAX_ID")
    private Long taxId;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "MODEL_ORGANISM")
    private BigInteger modelOrganism;
    @Column(name = "NUMBER_OF_PROTEINS")
    private BigInteger numberOfProteins;

    public EnzymePortalTaxonomy() {
    }

    public EnzymePortalTaxonomy(Long taxId) {
        this.taxId = taxId;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
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

    public BigInteger getModelOrganism() {
        return modelOrganism;
    }

    public void setModelOrganism(BigInteger modelOrganism) {
        this.modelOrganism = modelOrganism;
    }

    public BigInteger getNumberOfProteins() {
        return numberOfProteins;
    }

    public void setNumberOfProteins(BigInteger numberOfProteins) {
        this.numberOfProteins = numberOfProteins;
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
        if (!(object instanceof EnzymePortalTaxonomy)) {
            return false;
        }
        EnzymePortalTaxonomy other = (EnzymePortalTaxonomy) object;
        if ((this.taxId == null && other.taxId != null) || (this.taxId != null && !this.taxId.equals(other.taxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.dataservice.entities.EnzymePortalTaxonomy[ taxId=" + taxId + " ]";
    }
    
}
