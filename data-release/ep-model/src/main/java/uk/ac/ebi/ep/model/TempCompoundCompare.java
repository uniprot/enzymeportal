package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @Column(name = "COMPOUND_ID")
    private String compoundId;
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Column(name = "COMPOUND_SOURCE")
    private String compoundSource;
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Column(name = "UNIPROT_ACCESSION")
    private String uniprotAccession;
    @Column(name = "URL")
    private String url;
    @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
    @Column(name = "NOTE")
    private String note;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "COMPOUND_INTERNAL_ID")
      @SequenceGenerator(allocationSize = 1, name = "tmpSeqGenerator", sequenceName = "TEMP_COMPOUND_COMPARE_SEQ")
    @GeneratedValue(generator = "tmpSeqGenerator", strategy = GenerationType.AUTO)
    private Long compoundInternalId;

    public TempCompoundCompare() {
    }

    public TempCompoundCompare(Long compoundInternalId) {
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

    public Long getCompoundInternalId() {
        return compoundInternalId;
    }

    public void setCompoundInternalId(Long compoundInternalId) {
        this.compoundInternalId = compoundInternalId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.compoundId);
        hash = 37 * hash + Objects.hashCode(this.uniprotAccession);
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
        final TempCompoundCompare other = (TempCompoundCompare) obj;
        if (!Objects.equals(this.compoundId, other.compoundId)) {
            return false;
        }
        return Objects.equals(this.uniprotAccession, other.uniprotAccession);
    }



    @Override
    public String toString() {
        return "TempCompoundCompare{" + "compoundId=" + compoundId + ", compoundName=" + compoundName + ", compoundSource=" + compoundSource + ", relationship=" + relationship + ", uniprotAccession=" + uniprotAccession + ", url=" + url + ", compoundRole=" + compoundRole + ", note=" + note + '}';
    }


    
}
