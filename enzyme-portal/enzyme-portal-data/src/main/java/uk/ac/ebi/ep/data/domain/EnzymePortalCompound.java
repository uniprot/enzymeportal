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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.data.search.model.Compound;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
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
    @NamedQuery(name = "EnzymePortalCompound.findByUrl", query = "SELECT e FROM EnzymePortalCompound e WHERE e.url = :url"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundRole", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundRole = :compoundRole"),
    @NamedQuery(name = "EnzymePortalCompound.findByNote", query = "SELECT e FROM EnzymePortalCompound e WHERE e.note = :note")})
public class EnzymePortalCompound extends Compound implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPOUND_INTERNAL_ID")
    private Long compoundInternalId;
    @Size(max = 30)
    @Column(name = "COMPOUND_ID")
    private String compoundId;
    @Size(max = 4000)
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Size(max = 30)
    @Column(name = "COMPOUND_SOURCE")
    private String compoundSource;
    @Size(max = 30)
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Size(max = 255)
    @Column(name = "URL")
    private String url;
    @Size(max = 30)
    @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
    @Size(max = 4000)
    @Column(name = "NOTE")
    private String note;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalCompound() {
    }

    public EnzymePortalCompound(Long compoundInternalId) {
        this.compoundInternalId = compoundInternalId;
    }

    public EnzymePortalCompound(String compoundId, String compoundName, String url, String compoundRole) {
        super(compoundId, compoundName, url, compoundRole);
        this.compoundId = compoundId;
        this.compoundName = compoundName;
        this.compoundRole = compoundRole;
        this.url = url;
    }

    public EnzymePortalCompound(String compoundId, String compoundName, String compoundSource, String relationship, String compoundRole, String url, UniprotEntry uniprotAccession, String note) {
        this.compoundId = compoundId;
        this.compoundName = compoundName;
        this.compoundSource = compoundSource;
        this.relationship = relationship;
        this.compoundRole = compoundRole;
        this.url = url;
        this.uniprotAccession = uniprotAccession;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        hash += (compoundInternalId != null ? compoundInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalCompound)) {
            return false;
        }
        EnzymePortalCompound other = (EnzymePortalCompound) object;
        return !((this.compoundInternalId == null && other.compoundInternalId != null) || (this.compoundInternalId != null && !this.compoundInternalId.equals(other.compoundInternalId)));
    }

    @Override
    public String toString() {
        return "EnzymePortalCompound{" + "compoundName=" + compoundName + ", compoundRole=" + compoundRole + '}';
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
