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
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "ENZYME_PORTAL_PATHWAYS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalPathways.findAll", query = "SELECT e FROM EnzymePortalPathways e"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayInternalId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayInternalId = :pathwayInternalId"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayId = :pathwayId"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayUrl", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayUrl = :pathwayUrl"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayName", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayName = :pathwayName"),
    @NamedQuery(name = "EnzymePortalPathways.findByStatus", query = "SELECT e FROM EnzymePortalPathways e WHERE e.status = :status"),
    @NamedQuery(name = "EnzymePortalPathways.findBySpecies", query = "SELECT e FROM EnzymePortalPathways e WHERE e.species = :species"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayGroupId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayGroupId = :pathwayGroupId")})
public class EnzymePortalPathways implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PATHWAY_INTERNAL_ID")
    private BigDecimal pathwayInternalId;
    @Size(max = 15)
    @Column(name = "PATHWAY_ID")
    private String pathwayId;
    @Size(max = 255)
    @Column(name = "PATHWAY_URL")
    private String pathwayUrl;
    @Size(max = 4000)
    @Column(name = "PATHWAY_NAME")
    private String pathwayName;
    @Size(max = 5)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 255)
    @Column(name = "SPECIES")
    private String species;
    @Size(max = 15)
    @Column(name = "PATHWAY_GROUP_ID")
    private String pathwayGroupId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalPathways() {
    }

    public EnzymePortalPathways(BigDecimal pathwayInternalId) {
        this.pathwayInternalId = pathwayInternalId;
    }

    public BigDecimal getPathwayInternalId() {
        return pathwayInternalId;
    }

    public void setPathwayInternalId(BigDecimal pathwayInternalId) {
        this.pathwayInternalId = pathwayInternalId;
    }

    public String getPathwayId() {
        return pathwayId;
    }

    public void setPathwayId(String pathwayId) {
        this.pathwayId = pathwayId;
    }

    public String getPathwayUrl() {
        return pathwayUrl;
    }

    public void setPathwayUrl(String pathwayUrl) {
        this.pathwayUrl = pathwayUrl;
    }

    public String getPathwayName() {
        return pathwayName;
    }

    public void setPathwayName(String pathwayName) {
        this.pathwayName = pathwayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getPathwayGroupId() {
        return pathwayGroupId;
    }

    public void setPathwayGroupId(String pathwayGroupId) {
        this.pathwayGroupId = pathwayGroupId;
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
        hash += (pathwayInternalId != null ? pathwayInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalPathways)) {
            return false;
        }
        EnzymePortalPathways other = (EnzymePortalPathways) object;
        if ((this.pathwayInternalId == null && other.pathwayInternalId != null) || (this.pathwayInternalId != null && !this.pathwayInternalId.equals(other.pathwayInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.ep.model.EnzymePortalPathways[ pathwayInternalId=" + pathwayInternalId + " ]";
    }
    
}
