/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;


import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "ENZYME_PORTAL_NAMES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalNames.findAll", query = "SELECT e FROM EnzymePortalNames e"),
    @NamedQuery(name = "EnzymePortalNames.findByDbentryId", query = "SELECT e FROM EnzymePortalNames e WHERE e.dbentryId = :dbentryId"),
    @NamedQuery(name = "EnzymePortalNames.findByDescriptionType", query = "SELECT e FROM EnzymePortalNames e WHERE e.descriptionType = :descriptionType"),
    @NamedQuery(name = "EnzymePortalNames.findByCategoryType", query = "SELECT e FROM EnzymePortalNames e WHERE e.categoryType = :categoryType"),
    @NamedQuery(name = "EnzymePortalNames.findByName", query = "SELECT e FROM EnzymePortalNames e WHERE e.name = :name"),
    @NamedQuery(name = "EnzymePortalNames.findByEnzymeNameId", query = "SELECT e FROM EnzymePortalNames e WHERE e.enzymeNameId = :enzymeNameId")})
public class EnzymePortalNames implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION_TYPE")
    private String descriptionType;
    @Basic(optional = false)
    @Column(name = "CATEGORY_TYPE")
    private String categoryType;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ENZYME_NAME_ID")
    private BigDecimal enzymeNameId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false)
    private UniprotEntry uniprotAccession;

    public EnzymePortalNames() {
    }

    public EnzymePortalNames(BigDecimal enzymeNameId) {
        this.enzymeNameId = enzymeNameId;
    }

    public EnzymePortalNames(BigDecimal enzymeNameId, long dbentryId, String descriptionType, String categoryType, String name) {
        this.enzymeNameId = enzymeNameId;
        this.dbentryId = dbentryId;
        this.descriptionType = descriptionType;
        this.categoryType = categoryType;
        this.name = name;
    }

    public long getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(long dbentryId) {
        this.dbentryId = dbentryId;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getEnzymeNameId() {
        return enzymeNameId;
    }

    public void setEnzymeNameId(BigDecimal enzymeNameId) {
        this.enzymeNameId = enzymeNameId;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enzymeNameId != null ? enzymeNameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalNames)) {
            return false;
        }
        EnzymePortalNames other = (EnzymePortalNames) object;
        if ((this.enzymeNameId == null && other.enzymeNameId != null) || (this.enzymeNameId != null && !this.enzymeNameId.equals(other.enzymeNameId))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.data.domain.EnzymePortalNames[ enzymeNameId=" + enzymeNameId + " ]";
//    }

    @Override
    public String toString() {
        return "EnzymePortalNames{" + "descriptionType=" + descriptionType + ", categoryType=" + categoryType + ", name=" + name + ", enzymeNameId=" + enzymeNameId + ", uniprotAccession=" + uniprotAccession.getAccession() + '}';
    }
    
    
}
