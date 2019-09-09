package uk.ac.ebi.ep.model;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_CHEBI_COMPOUND")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalChebiCompound.findAll", query = "SELECT e FROM EnzymePortalChebiCompound e"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByCompoundInternalId", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.compoundInternalId = :compoundInternalId"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByCompoundId", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.compoundId = :compoundId"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByCompoundName", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.compoundName = :compoundName"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findBySynonyms", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.synonyms = :synonyms"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByRelationship", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.relationship = :relationship"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByUrl", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.url = :url"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByCompoundRole", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.compoundRole = :compoundRole"),
    @NamedQuery(name = "EnzymePortalChebiCompound.findByNote", query = "SELECT e FROM EnzymePortalChebiCompound e WHERE e.note = :note")})
public class EnzymePortalChebiCompound implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPOUND_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "chebiSeqGenerator", sequenceName = "SEQ_EZP_CHEBI_COMP")
    @GeneratedValue(generator = "chebiSeqGenerator", strategy = GenerationType.SEQUENCE)
    private Long compoundInternalId;
    @Size(max = 50)
    @Column(name = "COMPOUND_ID")
    private String compoundId;
    @Size(max = 50)
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Size(max = 4000)
    @Column(name = "SYNONYMS")
    private String synonyms;
    @Size(max = 50)
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Size(max = 250)
    @Column(name = "URL")
    private String url;
    @Size(max = 50)
    @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
    @Size(max = 4000)
    @Column(name = "NOTE")
    private String note;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalChebiCompound() {
    }

    public EnzymePortalChebiCompound(Long compoundInternalId) {
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

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
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
        if (!(object instanceof EnzymePortalChebiCompound)) {
            return false;
        }
        EnzymePortalChebiCompound other = (EnzymePortalChebiCompound) object;
        return !((this.compoundInternalId == null && other.compoundInternalId != null) || (this.compoundInternalId != null && !this.compoundInternalId.equals(other.compoundInternalId)));
    }

    @Override
    public String toString() {
        return "EnzymePortalChebiCompound{" + "compoundId=" + compoundId + ", compoundName=" + compoundName + ", compoundRole=" + compoundRole + '}';
    }

}
