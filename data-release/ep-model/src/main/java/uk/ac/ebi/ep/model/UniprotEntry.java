package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.StringUtils;

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
    @NamedQuery(name = "UniprotEntry.findByProteinName", query = "SELECT u FROM UniprotEntry u WHERE u.proteinName = :proteinName"),
    @NamedQuery(name = "UniprotEntry.findByScientificName", query = "SELECT u FROM UniprotEntry u WHERE u.scientificName = :scientificName"),
    @NamedQuery(name = "UniprotEntry.findByCommonName", query = "SELECT u FROM UniprotEntry u WHERE u.commonName = :commonName")
})

public class UniprotEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ACCESSION")
    private String accession;
    @Size(max = 30)
    @Column(name = "NAME")
    private String name;
    @Column(name = "TAX_ID")
    private Long taxId;
    @Size(max = 4000)
    @Column(name = "PROTEIN_NAME")
    private String proteinName;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "SEQUENCE_LENGTH")
    private Integer sequenceLength;
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTimestamp;
    @Size(max = 4000)
    @Column(name = "FUNCTION")
    private String function;
    @Column(name = "ENTRY_TYPE")
    private Short entryType;
    @Column(name = "FUNCTION_LENGTH")
    private BigInteger functionLength;
    @Size(max = 4000)
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;
    @Column(name = "UNCHARACTERIZED")
    private BigInteger uncharacterized;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;

    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalReactant> enzymePortalReactantSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession")
    private Set<UniprotXref> uniprotXrefSet;

    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession")
    private Set<EnzymePortalSummary> enzymePortalSummarySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalPathways> enzymePortalPathwaysSet;
    private static final String PDB = "PDB";
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalReaction> enzymePortalReactionSet;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<ChemblTargets> chemblTargetsSet;

    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalChebiCompound> enzymePortalChebiCompoundSet;

    public String getUniprotid() {
        return name;
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
        if (commonName == null || StringUtils.isEmpty(commonName)) {
            commonName = scientificName;
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
        if (expEvidenceFlag == null) {
            expEvidenceFlag = new BigInteger("1");
        }
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
    public Set<UniprotXref> getUniprotXrefSet() {
        return uniprotXrefSet;
    }

    public void setUniprotXrefSet(Set<UniprotXref> uniprotXrefSet) {
        this.uniprotXrefSet = uniprotXrefSet;
    }

    public List<String> getPdbeaccession() {

        return getPdbCodes(this);
    }

    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();

        e.getUniprotXrefSet()
                .stream()
                .filter(x -> x.getSource().equalsIgnoreCase(PDB))
                .limit(1)
                .forEach(xref -> pdbcodes.add(xref.getSourceId()));

        return pdbcodes;

    }

    @XmlTransient
    public Set<EnzymePortalEcNumbers> getEnzymePortalEcNumbersSet() {
        return enzymePortalEcNumbersSet;
    }

    public void setEnzymePortalEcNumbersSet(Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet) {
        this.enzymePortalEcNumbersSet = enzymePortalEcNumbersSet;
    }

    @XmlTransient
    public Set<EnzymePortalSummary> getEnzymePortalSummarySet() {
        return enzymePortalSummarySet;
    }

    public void setEnzymePortalSummarySet(Set<EnzymePortalSummary> enzymePortalSummarySet) {
        this.enzymePortalSummarySet = enzymePortalSummarySet;
    }

    @XmlTransient
    public Set<EnzymePortalCompound> getEnzymePortalCompoundSet() {
        return enzymePortalCompoundSet;
    }

    public void setEnzymePortalCompoundSet(Set<EnzymePortalCompound> enzymePortalCompoundSet) {
        this.enzymePortalCompoundSet = enzymePortalCompoundSet;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniprotEntry)) {
            return false;
        }
        UniprotEntry other = (UniprotEntry) object;
        return !((this.accession == null && other.accession != null) || (this.accession != null && !this.accession.equals(other.accession)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.ep.model.UniprotEntry[ accession=" + accession + " ]";
    }

    @XmlTransient
    public Set<EnzymePortalReaction> getEnzymePortalReactionSet() {
        return enzymePortalReactionSet;
    }

    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
        this.enzymePortalReactionSet = enzymePortalReactionSet;
    }

    @XmlTransient
    public Set<ChemblTargets> getChemblTargetsSet() {
        return chemblTargetsSet;
    }

    public void setChemblTargetsSet(Set<ChemblTargets> chemblTargetsSet) {
        this.chemblTargetsSet = chemblTargetsSet;
    }

    @XmlTransient
    public Set<EnzymePortalReactant> getEnzymePortalReactantSet() {
        return enzymePortalReactantSet;
    }

    public void setEnzymePortalReactantSet(Set<EnzymePortalReactant> enzymePortalReactantSet) {
        this.enzymePortalReactantSet = enzymePortalReactantSet;
    }

    @XmlTransient
    public Set<EnzymePortalChebiCompound> getEnzymePortalChebiCompoundSet() {
        return enzymePortalChebiCompoundSet;
    }

    public void setEnzymePortalChebiCompoundSet(Set<EnzymePortalChebiCompound> enzymePortalChebiCompoundSet) {
        this.enzymePortalChebiCompoundSet = enzymePortalChebiCompoundSet;
    }

}
