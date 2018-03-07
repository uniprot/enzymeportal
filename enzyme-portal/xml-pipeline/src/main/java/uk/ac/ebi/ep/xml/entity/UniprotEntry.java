/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniprotEntry.findAll", query = "SELECT u FROM UniprotEntry u"),
    @NamedQuery(name = "UniprotEntry.findByDbentryId", query = "SELECT u FROM UniprotEntry u WHERE u.dbentryId = :dbentryId"),
    @NamedQuery(name = "UniprotEntry.findByAccession", query = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession"),
    @NamedQuery(name = "UniprotEntry.findByName", query = "SELECT u FROM UniprotEntry u WHERE u.name = :name"),
    @NamedQuery(name = "UniprotEntry.findByTaxId", query = "SELECT u FROM UniprotEntry u WHERE u.taxId = :taxId"),
    @NamedQuery(name = "UniprotEntry.findByProteinName", query = "SELECT u FROM UniprotEntry u WHERE u.proteinName = :proteinName"),
    @NamedQuery(name = "UniprotEntry.findByScientificName", query = "SELECT u FROM UniprotEntry u WHERE u.scientificName = :scientificName"),
    @NamedQuery(name = "UniprotEntry.findByCommonName", query = "SELECT u FROM UniprotEntry u WHERE u.commonName = :commonName"),
    @NamedQuery(name = "UniprotEntry.findBySequenceLength", query = "SELECT u FROM UniprotEntry u WHERE u.sequenceLength = :sequenceLength"),
    @NamedQuery(name = "UniprotEntry.findByLastUpdateTimestamp", query = "SELECT u FROM UniprotEntry u WHERE u.lastUpdateTimestamp = :lastUpdateTimestamp"),
    @NamedQuery(name = "UniprotEntry.findByFunction", query = "SELECT u FROM UniprotEntry u WHERE u.function = :function"),
    @NamedQuery(name = "UniprotEntry.findByEntryType", query = "SELECT u FROM UniprotEntry u WHERE u.entryType = :entryType"),
    @NamedQuery(name = "UniprotEntry.findByFunctionLength", query = "SELECT u FROM UniprotEntry u WHERE u.functionLength = :functionLength"),
    @NamedQuery(name = "UniprotEntry.findBySynonymNames", query = "SELECT u FROM UniprotEntry u WHERE u.synonymNames = :synonymNames"),
    @NamedQuery(name = "UniprotEntry.findByExpEvidenceFlag", query = "SELECT u FROM UniprotEntry u WHERE u.expEvidenceFlag = :expEvidenceFlag"),
    @NamedQuery(name = "UniprotEntry.findByUncharacterized", query = "SELECT u FROM UniprotEntry u WHERE u.uncharacterized = :uncharacterized"),
    @NamedQuery(name = "UniprotEntry.findByPdbFlag", query = "SELECT u FROM UniprotEntry u WHERE u.pdbFlag = :pdbFlag")})
public class UniprotEntry implements Serializable {

    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EntryToGeneMapping> entryToGeneMappingSet;
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Id
    @Basic(optional = false)
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TAX_ID")
    private Long taxId;
    @Column(name = "PROTEIN_NAME")
    private String proteinName;
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "SEQUENCE_LENGTH")
    private Integer sequenceLength;
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTimestamp;
    @Column(name = "FUNCTION")
    private String function;
    @Column(name = "ENTRY_TYPE")
    private Short entryType;
    @Column(name = "FUNCTION_LENGTH")
    private BigInteger functionLength;
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;
    @Column(name = "UNCHARACTERIZED")
    private BigInteger uncharacterized;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet;
    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalDisease> enzymePortalDiseaseSet;
    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession", fetch = FetchType.LAZY)
    private Set<UniprotXref> uniprotXrefSet;
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID")
    @ManyToOne
    private ProteinGroups proteinGroupId;
    @JoinColumn(name = "RELATED_PROTEINS_ID", referencedColumnName = "REL_PROT_INTERNAL_ID")
    @ManyToOne
    private RelatedProteins relatedProteinsId;
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet;
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalPathways> enzymePortalPathwaysSet;

    public UniprotEntry() {
    }

    public UniprotEntry(String accession) {
        this.accession = accession;
    }

    public UniprotEntry(String accession, long dbentryId) {
        this.accession = accession;
        this.dbentryId = dbentryId;
    }

    public long getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(long dbentryId) {
        this.dbentryId = dbentryId;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Integer getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(Integer sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Short getEntryType() {
        return entryType;
    }

    public void setEntryType(Short entryType) {
        this.entryType = entryType;
    }

    public BigInteger getFunctionLength() {
        return functionLength;
    }

    public void setFunctionLength(BigInteger functionLength) {
        this.functionLength = functionLength;
    }

    public String getSynonymNames() {
        return synonymNames;
    }

    public void setSynonymNames(String synonymNames) {
        this.synonymNames = synonymNames;
    }

    public BigInteger getExpEvidenceFlag() {
        return expEvidenceFlag;
    }

    public void setExpEvidenceFlag(BigInteger expEvidenceFlag) {
        this.expEvidenceFlag = expEvidenceFlag;
    }

    public BigInteger getUncharacterized() {
        return uncharacterized;
    }

    public void setUncharacterized(BigInteger uncharacterized) {
        this.uncharacterized = uncharacterized;
    }

    public Character getPdbFlag() {
        return pdbFlag;
    }

    public void setPdbFlag(Character pdbFlag) {
        this.pdbFlag = pdbFlag;
    }

    @XmlTransient
    public Set<EnzymePortalEcNumbers> getEnzymePortalEcNumbersSet() {
        if (enzymePortalEcNumbersSet == null) {
            enzymePortalEcNumbersSet = new HashSet<>();
        }

        return enzymePortalEcNumbersSet;
    }

    public void setEnzymePortalEcNumbersSet(Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet) {
        this.enzymePortalEcNumbersSet = enzymePortalEcNumbersSet;
    }

    @XmlTransient
    public Set<EnzymePortalDisease> getEnzymePortalDiseaseSet() {
        if (enzymePortalDiseaseSet == null) {
            enzymePortalDiseaseSet = new HashSet<>();
        }

        return enzymePortalDiseaseSet;
    }

    public void setEnzymePortalDiseaseSet(Set<EnzymePortalDisease> enzymePortalDiseaseSet) {
        this.enzymePortalDiseaseSet = enzymePortalDiseaseSet;
    }

    @XmlTransient
    public Set<UniprotXref> getUniprotXrefSet() {
        if (uniprotXrefSet == null) {
            uniprotXrefSet = new HashSet<>();
        }

        return uniprotXrefSet;
    }

    public void setUniprotXrefSet(Set<UniprotXref> uniprotXrefSet) {
        this.uniprotXrefSet = uniprotXrefSet;
    }

    @XmlTransient
    public Set<EnzymePortalCompound> getEnzymePortalCompoundSet() {
        if (enzymePortalCompoundSet == null) {
            enzymePortalCompoundSet = new HashSet<>();
        }

        return enzymePortalCompoundSet;
    }

    public void setEnzymePortalCompoundSet(Set<EnzymePortalCompound> enzymePortalCompoundSet) {
        this.enzymePortalCompoundSet = enzymePortalCompoundSet;
    }

    public ProteinGroups getProteinGroupId() {
        return proteinGroupId;
    }

    public void setProteinGroupId(ProteinGroups proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public RelatedProteins getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(RelatedProteins relatedProteinsId) {
        this.relatedProteinsId = relatedProteinsId;
    }

    @XmlTransient
    public Set<EnzymeCatalyticActivity> getEnzymeCatalyticActivitySet() {
        if (enzymeCatalyticActivitySet == null) {
            enzymeCatalyticActivitySet = new HashSet<>();
        }
        return enzymeCatalyticActivitySet;
    }

    public void setEnzymeCatalyticActivitySet(Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet) {
        this.enzymeCatalyticActivitySet = enzymeCatalyticActivitySet;
    }

    @XmlTransient
    public Set<EnzymePortalPathways> getEnzymePortalPathwaysSet() {
        if (enzymePortalPathwaysSet == null) {
            enzymePortalPathwaysSet = new HashSet<>();
        }
        return enzymePortalPathwaysSet;
    }

    public void setEnzymePortalPathwaysSet(Set<EnzymePortalPathways> enzymePortalPathwaysSet) {
        this.enzymePortalPathwaysSet = enzymePortalPathwaysSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accession != null ? accession.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniprotEntry)) {
            return false;
        }
        UniprotEntry other = (UniprotEntry) object;
        return !((this.accession == null && other.accession != null) || (this.accession != null && !this.accession.equals(other.accession)));
    }

    @Override
    public String toString() {
        return "UniprotEntry{" + "accession=" + accession + ", name=" + name + ", taxId=" + taxId + ", proteinName=" + proteinName + ", scientificName=" + scientificName + ", commonName=" + commonName + ", sequenceLength=" + sequenceLength + ", lastUpdateTimestamp=" + lastUpdateTimestamp + ", function=" + function + ", entryType=" + entryType + ", functionLength=" + functionLength + ", synonymNames=" + synonymNames + ", expEvidenceFlag=" + expEvidenceFlag + ", uncharacterized=" + uncharacterized + ", pdbFlag=" + pdbFlag + ", proteinGroupId=" + proteinGroupId + ", relatedProteinsId=" + relatedProteinsId + '}';
    }

    @XmlTransient
    public Set<EntryToGeneMapping> getEntryToGeneMappingSet() {
        if (entryToGeneMappingSet == null) {
            entryToGeneMappingSet = new HashSet<>();
        }
        return entryToGeneMappingSet;
    }

    public void setEntryToGeneMappingSet(Set<EntryToGeneMapping> entryToGeneMappingSet) {
        this.entryToGeneMappingSet = entryToGeneMappingSet;
    }

}
