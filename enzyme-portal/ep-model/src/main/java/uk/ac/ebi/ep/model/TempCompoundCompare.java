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
@Table(name = "TEMP_COMPOUND_COMPARE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TempCompoundCompare.findAll", query = "SELECT t FROM TempCompoundCompare t"),
    @NamedQuery(name = "TempCompoundCompare.findByCompoundId", query = "SELECT t FROM TempCompoundCompare t WHERE t.compoundId = :compoundId"),
    @NamedQuery(name = "TempCompoundCompare.findByCompoundName", query = "SELECT t FROM TempCompoundCompare t WHERE t.compoundName = :compoundName"),
    @NamedQuery(name = "TempCompoundCompare.findByCompoundSource", query = "SELECT t FROM TempCompoundCompare t WHERE t.compoundSource = :compoundSource"),
    @NamedQuery(name = "TempCompoundCompare.findByRelationship", query = "SELECT t FROM TempCompoundCompare t WHERE t.relationship = :relationship"),
    @NamedQuery(name = "TempCompoundCompare.findByUniprotAccession", query = "SELECT t FROM TempCompoundCompare t WHERE t.uniprotAccession = :uniprotAccession"),
    @NamedQuery(name = "TempCompoundCompare.findByUrl", query = "SELECT t FROM TempCompoundCompare t WHERE t.url = :url"),
    @NamedQuery(name = "TempCompoundCompare.findByCompoundRole", query = "SELECT t FROM TempCompoundCompare t WHERE t.compoundRole = :compoundRole"),
    @NamedQuery(name = "TempCompoundCompare.findByNote", query = "SELECT t FROM TempCompoundCompare t WHERE t.note = :note"),
    @NamedQuery(name = "TempCompoundCompare.findByCompoundInternalId", query = "SELECT t FROM TempCompoundCompare t WHERE t.compoundInternalId = :compoundInternalId")})
public class TempCompoundCompare implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @Size(max = 15)
    @Column(name = "UNIPROT_ACCESSION")
    private String uniprotAccession;
    @Size(max = 255)
    @Column(name = "URL")
    private String url;
    @Size(max = 30)
    @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
    @Size(max = 4000)
    @Column(name = "NOTE")
    private String note;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPOUND_INTERNAL_ID")
    private BigDecimal compoundInternalId;

    public TempCompoundCompare() {
    }

    public TempCompoundCompare(BigDecimal compoundInternalId) {
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

    public String getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(String uniprotAccession) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getCompoundInternalId() {
        return compoundInternalId;
    }

    public void setCompoundInternalId(BigDecimal compoundInternalId) {
        this.compoundInternalId = compoundInternalId;
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
        if (!(object instanceof TempCompoundCompare)) {
            return false;
        }
        TempCompoundCompare other = (TempCompoundCompare) object;
        if ((this.compoundInternalId == null && other.compoundInternalId != null) || (this.compoundInternalId != null && !this.compoundInternalId.equals(other.compoundInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.ep.model.TempCompoundCompare[ compoundInternalId=" + compoundInternalId + " ]";
    }
    
}
