package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import uk.ac.ebi.ep.model.dao.Compound;

/**
 *
 * @author joseph
 */
@Entity
//@BatchSize(size = 20)
@Table(name = "ENZYME_PORTAL_COMPOUND")
@XmlRootElement

@NamedEntityGraph(name = "CompoundEntityGraph", attributeNodes = {
    @NamedAttributeNode("uniprotAccession")
})

@SqlResultSetMapping(
        name = "compoundMapping",
        classes = {
            @ConstructorResult(
                    targetClass = Compound.class,
                    columns = {
                        @ColumnResult(name = "COMPOUND_ID")
                        ,
                        @ColumnResult(name = "COMPOUND_NAME")
                        ,
                        @ColumnResult(name = "URL")
                        ,
                        @ColumnResult(name = "COMPOUND_ROLE")
                    }
            )
        }
)

@NamedQueries({
    @NamedQuery(name = "EnzymePortalCompound.findAll", query = "SELECT e FROM EnzymePortalCompound e")
    ,
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundInternalId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundInternalId = :compoundInternalId")
    ,
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundId = :compoundId")
    ,
    // @NamedQuery(name = "EnzymePortalCompound.findByCompoundName", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundName = :compoundName"),
    @NamedQuery(name = "EnzymePortalCompound.findByCompoundSource", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundSource = :compoundSource")
    ,
    @NamedQuery(name = "EnzymePortalCompound.findByRelationship", query = "SELECT e FROM EnzymePortalCompound e WHERE e.relationship = :relationship")
    ,
    @NamedQuery(name = "EnzymePortalCompound.findByUniprotAccession", query = "SELECT e FROM EnzymePortalCompound e WHERE e.uniprotAccession = :uniprotAccession")})
//public class EnzymePortalCompound extends Compound implements Serializable {
public class EnzymePortalCompound implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "COMPOUND_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "cpSeqGenerator", sequenceName = "SEQ_COMPOUND_INTERNAL_ID")
    @GeneratedValue(generator = "cpSeqGenerator", strategy = GenerationType.SEQUENCE)
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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    @JoinTable(name = "COMPOUND_TO_REACTION", joinColumns = {
        @JoinColumn(name = "COMPOUND_INTERNAL_ID", referencedColumnName = "COMPOUND_INTERNAL_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "REACTION_INTERNAL_ID", referencedColumnName = "REACTION_INTERNAL_ID")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<EnzymePortalReaction> enzymePortalReactionSet;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CHEMBL_TARGET_ID")
    private String chemblTargetId;

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

    public EnzymePortalCompound(String compoundId, String compoundName, String url, String compoundRole) {
        //super(compoundId, compoundName, url, compoundRole);
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
        return Objects.equals(this.compoundId, other.compoundId);
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    //@Override
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
        if (enzymePortalReactionSet == null) {
            enzymePortalReactionSet = new HashSet<>();
        }
        return this.enzymePortalReactionSet;
    }

    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
        this.enzymePortalReactionSet = enzymePortalReactionSet;
    }

    //@Override
    public String getId() {
        return compoundId;
    }

    //@Override
    public String getName() {
        return compoundName;
    }

    //@Override
    public Compound.Role getRole() {
        return Compound.Role.valueOf(compoundRole);

    }

//    @Override
//    public boolean isSelected() {
//        return selected;
//    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getChemblTargetId() {
        
        if(chemblTargetId == null){
            chemblTargetId="";
        }
        return chemblTargetId;
    }

    public void setChemblTargetId(String chemblTargetId) {
        this.chemblTargetId = chemblTargetId;
    }
    
    

    @Override
    public String toString() {
        return "EnzymePortalCompound{" + "compoundId=" + compoundId + ", compoundName=" + compoundName + ", compoundSource=" + compoundSource + ", relationship=" + relationship + ", compoundRole=" + compoundRole + ", note=" + note + '}';
    }

}
