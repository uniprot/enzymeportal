/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "ENZYME_PORTAL_COMPOUND")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalCompound.findAll", query = "SELECT e FROM EnzymePortalCompound e"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundInternalId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundInternalId = :compoundInternalId"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundId = :compoundId"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundName", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundName = :compoundName"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundSource", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundSource = :compoundSource"),
    @NamedQuery(name = "EnzymePortalCompound.findByRelationship", query = "SELECT e FROM EnzymePortalCompound e WHERE e.relationship = :relationship"),
    @NamedQuery(name = "EnzymePortalCompound.findByUniprotAccession", query = "SELECT e FROM EnzymePortalCompound e WHERE e.uniprotAccession = :uniprotAccession")})
public class EnzymePortalCompound implements Serializable {
    @JoinTable(name = "COMPOUND_TO_REACTION", joinColumns = {
        @JoinColumn(name = "COMPOUND_INTERNAL_ID", referencedColumnName = "COMPOUND_INTERNAL_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "REACTION_INTERNAL_ID", referencedColumnName = "REACTION_INTERNAL_ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Reaction> reactionSet = new HashSet<>();

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "COMPOUND_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_COMPOUND_INTERNAL_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private Long compoundInternalId;
    @Column(name = "COMPOUND_ID")
    private String compoundId;
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Column(name = "COMPOUND_SOURCE")
    private String compoundSource;
    @Column(name = "RELATIONSHIP")
    private String relationship;

    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(fetch = FetchType.EAGER)
    private UniprotEntry uniprotAccession;

    public EnzymePortalCompound() {
    }

    public EnzymePortalCompound(Long compoundInternalId) {
        this.compoundInternalId = compoundInternalId;
    }

    public Long getCompoundInternalId() {
        return compoundInternalId;
    }

    public void setCompoundInternalId(Long compoundInternalId) {
        this.compoundInternalId = compoundInternalId;
    }

    public String getCompoundId() {
        return compoundId;
    }

    public void setCompoundId(String compoundId) {
        this.compoundId = compoundId;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getCompoundSource() {
        return compoundSource;
    }

    public void setCompoundSource(String compoundSource) {
        this.compoundSource = compoundSource;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.compoundInternalId);
        hash = 53 * hash + Objects.hashCode(this.compoundId);
        hash = 53 * hash + Objects.hashCode(this.compoundName);
        hash = 53 * hash + Objects.hashCode(this.compoundSource);
        hash = 53 * hash + Objects.hashCode(this.relationship);
        hash = 53 * hash + Objects.hashCode(this.uniprotAccession);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymePortalCompound other = (EnzymePortalCompound) obj;
        if (!Objects.equals(this.compoundInternalId, other.compoundInternalId)) {
            return false;
        }
        if (!Objects.equals(this.compoundId, other.compoundId)) {
            return false;
        }
        if (!Objects.equals(this.compoundName, other.compoundName)) {
            return false;
        }
        if (!Objects.equals(this.compoundSource, other.compoundSource)) {
            return false;
        }
        if (!Objects.equals(this.relationship, other.relationship)) {
            return false;
        }
        if (!Objects.equals(this.uniprotAccession, other.uniprotAccession)) {
            return false;
        }
        return true;
    }



//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (compoundInternalId != null ? compoundInternalId.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof EnzymePortalCompound)) {
//            return false;
//        }
//        EnzymePortalCompound other = (EnzymePortalCompound) object;
//        if ((this.compoundInternalId == null && other.compoundInternalId != null) || (this.compoundInternalId != null && !this.compoundInternalId.equals(other.compoundInternalId))) {
//            return false;
//        }
//        return true;
//    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.data.domain.EnzymePortalCompound[ compoundInternalId=" + compoundInternalId + " ]";
//    }
    @Override
    public String toString() {
        return "EnzymePortalCompound{" + "reactionSet=" + reactionSet + ", compoundInternalId=" + compoundInternalId + ", compoundId=" + compoundId + ", compoundName=" + compoundName + ", compoundSource=" + compoundSource + ", relationship=" + relationship + ", uniprotAccession=" + uniprotAccession + '}';
    }

    
    
    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @XmlTransient
    public Set<Reaction> getReactionSet() {
        return reactionSet;
    }

    public void setReactionSet(Set<Reaction> reactionSet) {
        this.reactionSet = reactionSet;
    }

}
