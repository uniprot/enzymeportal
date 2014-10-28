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
import uk.ac.ebi.ep.data.search.model.Compound;


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
   // @NamedQuery(name = "EnzymePortalCompound.findByCompoundName", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundName = :compoundName"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundSource", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundSource = :compoundSource"),
    @NamedQuery(name = "EnzymePortalCompound.findByRelationship", query = "SELECT e FROM EnzymePortalCompound e WHERE e.relationship = :relationship"),
    @NamedQuery(name = "EnzymePortalCompound.findByUniprotAccession", query = "SELECT e FROM EnzymePortalCompound e WHERE e.uniprotAccession = :uniprotAccession")})
public class EnzymePortalCompound extends Compound implements Serializable {

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

        @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
 
    @Column(name = "URL")
    private String url;

    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;
    
        @JoinTable(name = "COMPOUND_TO_REACTION", joinColumns = {
        @JoinColumn(name = "COMPOUND_INTERNAL_ID", referencedColumnName = "COMPOUND_INTERNAL_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "REACTION_INTERNAL_ID", referencedColumnName = "REACTION_INTERNAL_ID")})
    @ManyToMany
    private Set<EnzymePortalReaction> enzymePortalReactionSet;


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
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.compoundId);
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
        if (!Objects.equals(this.compoundId, other.compoundId)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "EnzymePortalCompound{" + "compoundId=" + compoundId + ", compoundName=" + compoundName + ", compoundSource=" + compoundSource + ", relationship=" + relationship + ", compoundRole=" + compoundRole + '}';
    }



    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getCompoundRole() {
        return compoundRole;
    }

    public void setCompoundRole(String compoundRole) {
        this.compoundRole = compoundRole;
    }

    @XmlTransient
    public Set<EnzymePortalReaction> getEnzymePortalReactionSet() {
        if(enzymePortalReactionSet == null){
            enzymePortalReactionSet = new HashSet<>();
        }
        return this.enzymePortalReactionSet;
    }

    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
        this.enzymePortalReactionSet = enzymePortalReactionSet;
    }

    @Override
  public String getId() {
        return compoundId;
    }


    @Override
    public String getName() {
        return compoundName;
    }
    
    @Override
      public Compound.Role getRole() {
        return Compound.Role.valueOf(compoundRole);

    }


    @Override
    public boolean isSelected() {
        return selected;
    }



 

}
