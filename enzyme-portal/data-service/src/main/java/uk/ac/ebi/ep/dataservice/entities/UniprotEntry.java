package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement

@NamedQuery(name = "UniprotEntry.findAll", query = "SELECT u FROM UniprotEntry u")
@NamedQuery(name = "UniprotEntry.findByDbentryId", query = "SELECT u FROM UniprotEntry u WHERE u.dbentryId = :dbentryId")
@NamedQuery(name = "UniprotEntry.findByAccession", query = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession")
@NamedQuery(name = "UniprotEntry.findByName", query = "SELECT u FROM UniprotEntry u WHERE u.name = :name")
@NamedQuery(name = "UniprotEntry.findByTaxId", query = "SELECT u FROM UniprotEntry u WHERE u.taxId = :taxId")
@NamedQuery(name = "UniprotEntry.findByProteinName", query = "SELECT u FROM UniprotEntry u WHERE u.proteinName = :proteinName")
@NamedQuery(name = "UniprotEntry.findByScientificName", query = "SELECT u FROM UniprotEntry u WHERE u.scientificName = :scientificName")
@NamedQuery(name = "UniprotEntry.findByCommonName", query = "SELECT u FROM UniprotEntry u WHERE u.commonName = :commonName")
@NamedQuery(name = "UniprotEntry.findBySequenceLength", query = "SELECT u FROM UniprotEntry u WHERE u.sequenceLength = :sequenceLength")
@NamedQuery(name = "UniprotEntry.findByLastUpdateTimestamp", query = "SELECT u FROM UniprotEntry u WHERE u.lastUpdateTimestamp = :lastUpdateTimestamp")
@NamedQuery(name = "UniprotEntry.findByFunction", query = "SELECT u FROM UniprotEntry u WHERE u.function = :function")
@NamedQuery(name = "UniprotEntry.findByEntryType", query = "SELECT u FROM UniprotEntry u WHERE u.entryType = :entryType")
@NamedQuery(name = "UniprotEntry.findByFunctionLength", query = "SELECT u FROM UniprotEntry u WHERE u.functionLength = :functionLength")
@NamedQuery(name = "UniprotEntry.findBySynonymNames", query = "SELECT u FROM UniprotEntry u WHERE u.synonymNames = :synonymNames")
@NamedQuery(name = "UniprotEntry.findByExpEvidenceFlag", query = "SELECT u FROM UniprotEntry u WHERE u.expEvidenceFlag = :expEvidenceFlag")
@NamedQuery(name = "UniprotEntry.findByUncharacterized", query = "SELECT u FROM UniprotEntry u WHERE u.uncharacterized = :uncharacterized")
@NamedQuery(name = "UniprotEntry.findByPdbFlag", query = "SELECT u FROM UniprotEntry u WHERE u.pdbFlag = :pdbFlag")
public class UniprotEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ACCESSION")
    private String accession;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Size(max = 30)
    @Column(name = "NAME")
    private String name;
    @Size(max = 4000)
    @Column(name = "PROTEIN_NAME")
    private String proteinName;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Size(max = 4000)
    @Column(name = "FUNCTION")
    private String function;
    @Size(max = 4000)
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;
    @Column(name = "TAX_ID")
    private Long taxId;
    @Column(name = "SEQUENCE_LENGTH")
    private Integer sequenceLength;
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTimestamp;
    @Column(name = "ENTRY_TYPE")
    private Short entryType;
    @Column(name = "FUNCTION_LENGTH")
    private BigInteger functionLength;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;
    @Column(name = "UNCHARACTERIZED")
    private BigInteger uncharacterized;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;
    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalReaction> enzymePortalReactionSet;
    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession")
    private Set<EnzymePortalDisease> enzymePortalDiseaseSet;
    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EntryToGeneMapping> entryToGeneMappingSet;
    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @JoinColumn(name = "RELATED_PROTEINS_ID", referencedColumnName = "REL_PROT_INTERNAL_ID")
    @ManyToOne
    private RelatedProteins relatedProteinsId;
    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalPathways> enzymePortalPathwaysSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession")
    private Set<UniprotXref> uniprotXrefSet;

    public UniprotEntry() {
    }

    public UniprotEntry(String accession) {
        this.accession = accession;
    }

    public UniprotEntry(long dbentryId) {
        this.dbentryId = dbentryId;
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
        if (commonName == null) {
            return getScientificName();
        }

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
    public Set<EnzymePortalReaction> getEnzymePortalReactionSet() {
        return enzymePortalReactionSet;
    }

    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
        this.enzymePortalReactionSet = enzymePortalReactionSet;
    }

    @XmlTransient
    public Set<EnzymePortalEcNumbers> getEnzymePortalEcNumbersSet() {
        return enzymePortalEcNumbersSet;
    }

    public void setEnzymePortalEcNumbersSet(Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet) {
        this.enzymePortalEcNumbersSet = enzymePortalEcNumbersSet;
    }

    @XmlTransient
    public Set<EnzymePortalDisease> getEnzymePortalDiseaseSet() {
        return enzymePortalDiseaseSet;
    }

    public void setEnzymePortalDiseaseSet(Set<EnzymePortalDisease> enzymePortalDiseaseSet) {
        this.enzymePortalDiseaseSet = enzymePortalDiseaseSet;
    }

    @XmlTransient
    public Set<EntryToGeneMapping> getEntryToGeneMappingSet() {
        return entryToGeneMappingSet;
    }

    public void setEntryToGeneMappingSet(Set<EntryToGeneMapping> entryToGeneMappingSet) {
        this.entryToGeneMappingSet = entryToGeneMappingSet;
    }

    @XmlTransient
    public Set<EnzymePortalCompound> getEnzymePortalCompoundSet() {
        return enzymePortalCompoundSet;
    }

    public void setEnzymePortalCompoundSet(Set<EnzymePortalCompound> enzymePortalCompoundSet) {
        this.enzymePortalCompoundSet = enzymePortalCompoundSet;
    }

    public RelatedProteins getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(RelatedProteins relatedProteinsId) {
        this.relatedProteinsId = relatedProteinsId;
    }

    @XmlTransient
    public Set<EnzymePortalPathways> getEnzymePortalPathwaysSet() {
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
        if (!(object instanceof UniprotEntry)) {
            return false;
        }
        UniprotEntry other = (UniprotEntry) object;
        return !((this.accession == null && other.accession != null) || (this.accession != null && !this.accession.equals(other.accession)));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UniprotEntry{");
        sb.append("entryType=").append(entryType);
        sb.append(", accession=").append(accession);
        sb.append(", name='").append(name).append('\'');
        sb.append(", taxId='").append(taxId).append('\'');
        sb.append(", proteinName=").append(proteinName);
        sb.append(", scientificName=").append(scientificName);
        sb.append(", commonName=").append(commonName);
        sb.append(", expEvidenceFlag=").append(expEvidenceFlag);
        sb.append('}');
        return sb.toString();
    }

    public List<String> getSynonym() {

        Optional<String> synonymName = Optional.ofNullable(this.getSynonymNames());
        return Stream.of(synonymName.orElse("").split(";"))
                .filter(otherName -> (!otherName.trim().equalsIgnoreCase(getProteinName().trim())))
                .collect(Collectors.toList());

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @XmlTransient
    public Set<UniprotXref> getUniprotXrefSet() {
        return uniprotXrefSet;
    }

    public void setUniprotXrefSet(Set<UniprotXref> uniprotXrefSet) {
        this.uniprotXrefSet = uniprotXrefSet;
    }

}
