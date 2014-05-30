/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "REACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reaction.findAll", query = "SELECT r FROM Reaction r"),
    @NamedQuery(name = "Reaction.findByReactionInternalId", query = "SELECT r FROM Reaction r WHERE r.reactionInternalId = :reactionInternalId"),
    @NamedQuery(name = "Reaction.findByReactionId", query = "SELECT r FROM Reaction r WHERE r.reactionId = :reactionId"),
    @NamedQuery(name = "Reaction.findByReactionName", query = "SELECT r FROM Reaction r WHERE r.reactionName = :reactionName"),
    @NamedQuery(name = "Reaction.findByRelationship", query = "SELECT r FROM Reaction r WHERE r.relationship = :relationship")})
public class Reaction implements Serializable {
    @ManyToMany(mappedBy = "reactionSet", fetch = FetchType.EAGER)
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @Column(name = "REACTION_SOURCE")
    private String reactionSource;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REACTION_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_REACTION_INTERNAL_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private Long reactionInternalId;
    @Column(name = "REACTION_ID")
    private String reactionId;
    @Column(name = "REACTION_NAME")
    private String reactionName;
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(fetch = FetchType.EAGER)
    private UniprotEntry uniprotAccession;

    public Reaction() {
    }

    public Reaction(Long reactionInternalId) {
        this.reactionInternalId = reactionInternalId;
    }

    public Long getReactionInternalId() {
        return reactionInternalId;
    }

    public void setReactionInternalId(Long reactionInternalId) {
        this.reactionInternalId = reactionInternalId;
    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public String getReactionName() {
        return reactionName;
    }

    public void setReactionName(String reactionName) {
        this.reactionName = reactionName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
        hash += (reactionInternalId != null ? reactionInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reaction)) {
            return false;
        }
        Reaction other = (Reaction) object;
        if ((this.reactionInternalId == null && other.reactionInternalId != null) || (this.reactionInternalId != null && !this.reactionInternalId.equals(other.reactionInternalId))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.data.domain.Reaction[ reactionInternalId=" + reactionInternalId + " ]";
//    }
    @Override
    public String toString() {
        return "Reaction{" + "reactionSource=" + reactionSource + ", reactionInternalId=" + reactionInternalId + ", reactionId=" + reactionId + ", reactionName=" + reactionName + ", relationship=" + relationship + ", uniprotAccession=" + uniprotAccession + '}';
    }
    
    
    

    public String getReactionSource() {
        return reactionSource;
    }

    public void setReactionSource(String reactionSource) {
        this.reactionSource = reactionSource;
    }

    @XmlTransient
    public Set<EnzymePortalCompound> getEnzymePortalCompoundSet() {
        return enzymePortalCompoundSet;
    }

    public void setEnzymePortalCompoundSet(Set<EnzymePortalCompound> enzymePortalCompoundSet) {
        this.enzymePortalCompoundSet = enzymePortalCompoundSet;
    }
    
}
