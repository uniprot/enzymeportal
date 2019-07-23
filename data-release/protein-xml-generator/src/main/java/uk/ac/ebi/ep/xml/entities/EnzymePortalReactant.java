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
@Table(name = "ENZYME_PORTAL_REACTANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalReactant.findAll", query = "SELECT e FROM EnzymePortalReactant e"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantInternalId", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantInternalId = :reactantInternalId"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantId", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantId = :reactantId"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantName", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantName = :reactantName"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantSource", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantSource = :reactantSource"),
    @NamedQuery(name = "EnzymePortalReactant.findByRelationship", query = "SELECT e FROM EnzymePortalReactant e WHERE e.relationship = :relationship"),
    @NamedQuery(name = "EnzymePortalReactant.findByUrl", query = "SELECT e FROM EnzymePortalReactant e WHERE e.url = :url"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantRole", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantRole = :reactantRole"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactionDirection", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactionDirection = :reactionDirection")})
public class EnzymePortalReactant implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REACTANT_INTERNAL_ID")
    private BigDecimal reactantInternalId;
    @Column(name = "REACTANT_ID")
    private String reactantId;
    @Column(name = "REACTANT_NAME")
    private String reactantName;
    @Column(name = "REACTANT_SOURCE")
    private String reactantSource;
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Column(name = "URL")
    private String url;
    @Column(name = "REACTANT_ROLE")
    private String reactantRole;
    @Column(name = "REACTION_DIRECTION")
    private String reactionDirection;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalReactant() {
    }

    public EnzymePortalReactant(BigDecimal reactantInternalId) {
        this.reactantInternalId = reactantInternalId;
    }

    public BigDecimal getReactantInternalId() {
        return reactantInternalId;
    }

    public void setReactantInternalId(BigDecimal reactantInternalId) {
        this.reactantInternalId = reactantInternalId;
    }

    public String getReactantId() {
        return reactantId;
    }

    public void setReactantId(String reactantId) {
        this.reactantId = reactantId;
    }

    public String getReactantName() {
        return reactantName;
    }

    public void setReactantName(String reactantName) {
        this.reactantName = reactantName;
    }

    public String getReactantSource() {
        return reactantSource;
    }

    public void setReactantSource(String reactantSource) {
        this.reactantSource = reactantSource;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReactantRole() {
        return reactantRole;
    }

    public void setReactantRole(String reactantRole) {
        this.reactantRole = reactantRole;
    }

    public String getReactionDirection() {
        return reactionDirection;
    }

    public void setReactionDirection(String reactionDirection) {
        this.reactionDirection = reactionDirection;
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
        hash += (reactantInternalId != null ? reactantInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalReactant)) {
            return false;
        }
        EnzymePortalReactant other = (EnzymePortalReactant) object;
        if ((this.reactantInternalId == null && other.reactantInternalId != null) || (this.reactantInternalId != null && !this.reactantInternalId.equals(other.reactantInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.xml.entities.EnzymePortalReactant[ reactantInternalId=" + reactantInternalId + " ]";
    }
    
}
