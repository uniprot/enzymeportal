/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_PATHWAYS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalPathways.findAll", query = "SELECT e FROM EnzymePortalPathways e"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayInternalId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayInternalId = :pathwayInternalId"),
    //@NamedQuery(name = "EnzymePortalPathways.findByPathwayId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayId = :pathwayId"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayUrl", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayUrl = :pathwayUrl"),
    @NamedQuery(name = "EnzymePortalPathways.findByPathwayName", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayName = :pathwayName"),
    @NamedQuery(name = "EnzymePortalPathways.findByStatus", query = "SELECT e FROM EnzymePortalPathways e WHERE e.status = :status"),
    @NamedQuery(name = "EnzymePortalPathways.findBySpecies", query = "SELECT e FROM EnzymePortalPathways e WHERE e.species = :species")})
public class EnzymePortalPathways implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PATHWAY_INTERNAL_ID") 
     @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_PATHWAY_INTERNAL_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private Long pathwayInternalId;
    @Column(name = "PATHWAY_ID")
    private String pathwayId;
    @Column(name = "PATHWAY_URL")
    private String pathwayUrl;
    @Column(name = "PATHWAY_NAME")
    private String pathwayName;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "SPECIES")
    private String species;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalPathways() {
    }

    public EnzymePortalPathways(Long pathwayInternalId) {
        this.pathwayInternalId = pathwayInternalId;
    }

    public Long getPathwayInternalId() {
        return pathwayInternalId;
    }

    public void setPathwayInternalId(Long pathwayInternalId) {
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
        return "EnzymePortalPathways{" + "pathwayId=" + pathwayId + ", pathwayUrl=" + pathwayUrl + ", pathwayName=" + pathwayName + ", status=" + status + ", species=" + species + ", uniprotAccession=" + uniprotAccession + '}';
    }

    
}
