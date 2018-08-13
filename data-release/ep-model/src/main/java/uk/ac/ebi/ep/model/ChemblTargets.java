/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseph
 */
@Entity
@Table(name = "CHEMBL_TARGETS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChemblTargets.findAll", query = "SELECT c FROM ChemblTargets c")
    , @NamedQuery(name = "ChemblTargets.findByInternalId", query = "SELECT c FROM ChemblTargets c WHERE c.internalId = :internalId")
    , @NamedQuery(name = "ChemblTargets.findByChemblId", query = "SELECT c FROM ChemblTargets c WHERE c.chemblId = :chemblId")
    , @NamedQuery(name = "ChemblTargets.findByComponentType", query = "SELECT c FROM ChemblTargets c WHERE c.componentType = :componentType")})
public class ChemblTargets implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    @Size(max = 20)
    @Column(name = "CHEMBL_ID")
    private String chemblId;
    @Size(max = 20)
    @Column(name = "COMPONENT_TYPE")
    private String componentType;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public ChemblTargets() {
    }

    public ChemblTargets(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public BigDecimal getInternalId() {
        return internalId;
    }

    public void setInternalId(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public String getChemblId() {
        return chemblId;
    }

    public void setChemblId(String chemblId) {
        this.chemblId = chemblId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
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
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChemblTargets)) {
            return false;
        }
        ChemblTargets other = (ChemblTargets) object;
        if ((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.model.ChemblTargets[ internalId=" + internalId + " ]";
    }
    
}
