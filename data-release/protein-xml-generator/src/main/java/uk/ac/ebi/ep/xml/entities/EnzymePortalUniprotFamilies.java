/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
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
@Table(name = "ENZYME_PORTAL_UNIPROT_FAMILIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalUniprotFamilies.findAll", query = "SELECT e FROM EnzymePortalUniprotFamilies e"),
    @NamedQuery(name = "EnzymePortalUniprotFamilies.findByDbentryId", query = "SELECT e FROM EnzymePortalUniprotFamilies e WHERE e.dbentryId = :dbentryId"),
    @NamedQuery(name = "EnzymePortalUniprotFamilies.findByFamilyName", query = "SELECT e FROM EnzymePortalUniprotFamilies e WHERE e.familyName = :familyName"),
    //@NamedQuery(name = "EnzymePortalUniprotFamilies.findByUniprotFamilyId", query = "SELECT e FROM EnzymePortalUniprotFamilies e WHERE e.uniprotFamilyId = :uniprotFamilyId"),
    @NamedQuery(name = "EnzymePortalUniprotFamilies.findByFamilyGroupId", query = "SELECT e FROM EnzymePortalUniprotFamilies e WHERE e.familyGroupId = :familyGroupId")})
public class EnzymePortalUniprotFamilies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "DBENTRY_ID")
    private String dbentryId;
    @Column(name = "FAMILY_NAME")
    private String familyName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "UNIPROT_FAMILY_ID")
    private BigDecimal uniprotFamilyId;
    @Column(name = "FAMILY_GROUP_ID")
    private String familyGroupId;
    @JoinColumn(name = "ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry accession;

    public EnzymePortalUniprotFamilies() {
    }

    public EnzymePortalUniprotFamilies(BigDecimal uniprotFamilyId) {
        this.uniprotFamilyId = uniprotFamilyId;
    }

    public String getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(String dbentryId) {
        this.dbentryId = dbentryId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public BigDecimal getUniprotFamilyId() {
        return uniprotFamilyId;
    }

    public void setUniprotFamilyId(BigDecimal uniprotFamilyId) {
        this.uniprotFamilyId = uniprotFamilyId;
    }

    public String getFamilyGroupId() {
        return familyGroupId;
    }

    public void setFamilyGroupId(String familyGroupId) {
        this.familyGroupId = familyGroupId;
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
        hash += (uniprotFamilyId != null ? uniprotFamilyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalUniprotFamilies)) {
            return false;
        }
        EnzymePortalUniprotFamilies other = (EnzymePortalUniprotFamilies) object;
        if ((this.uniprotFamilyId == null && other.uniprotFamilyId != null) || (this.uniprotFamilyId != null && !this.uniprotFamilyId.equals(other.uniprotFamilyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.xml.entities.EnzymePortalUniprotFamilies[ uniprotFamilyId=" + uniprotFamilyId + " ]";
    }
    
}
