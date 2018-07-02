package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
import uk.ac.ebi.ep.data.common.ModelOrganisms;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.Species;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement

@NamedEntityGraph(name = "UniprotEntryEntityGraph", attributeNodes = {
    // @NamedAttributeNode(value = "relatedProteinsId", subgraph = "uniprotEntrySet"),
    @NamedAttributeNode("enzymePortalPathwaysSet"),

    @NamedAttributeNode(value = "enzymePortalCompoundSet", subgraph = "enzymePortalCompoundSet"),
    @NamedAttributeNode("enzymePortalDiseaseSet"),
    @NamedAttributeNode("uniprotXrefSet"),
    @NamedAttributeNode("enzymePortalEcNumbersSet"),
    @NamedAttributeNode("enzymeCatalyticActivitySet")
}
)

@NamedQueries({
    @NamedQuery(name = "UniprotEntry.findAll", query = "SELECT u FROM UniprotEntry u"),
    @NamedQuery(name = "UniprotEntry.findByDbentryId", query = "SELECT u FROM UniprotEntry u WHERE u.dbentryId = :dbentryId"),
    //@NamedQuery(name = "UniprotEntry.findByAccession", query = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession"),
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
public class UniprotEntry extends EnzymeAccession implements Serializable, Comparable<UniprotEntry> {

    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalReaction> enzymePortalReactionSet;

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
    @OneToMany(mappedBy = "uniprotAccession")
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalDisease> enzymePortalDiseaseSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession")
    @Fetch(FetchMode.JOIN)
    private Set<UniprotXref> uniprotXrefSet;
    @OneToMany(mappedBy = "uniprotAccession")
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID")
    @ManyToOne
    private ProteinGroups proteinGroupId;
    @JoinColumn(name = "RELATED_PROTEINS_ID", referencedColumnName = "REL_PROT_INTERNAL_ID")
    @ManyToOne
    private RelatedProteins relatedProteinsId;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.EAGER)
    private Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet;
    @OneToMany(mappedBy = "uniprotAccession")
    private Set<EnzymePortalPathways> enzymePortalPathwaysSet;

//    @Transient
//    protected String PDB_SOURCE = "PDB";
//    @Transient
//    protected int PDB_CODE_LIMIT = 500;
//    @Transient
//    protected int SORTED_SPECIES_LIMIT = 100;
//    @Transient
//    private Boolean expEvidence;
//    @Transient
//    private Species species;
//    @Transient
//    protected transient List<String> pdbeaccession;
//    @Transient
//    protected transient List<Compound> compounds;
//    @Transient
//    protected transient List<Disease> diseases;
//    @Transient
//    protected Boolean scoring = Boolean.FALSE;
//    @Transient
//    protected Float identity = 0.0f;
//    @Transient
//    protected Integer score = 0;
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
    public Set<UniprotXref> getUniprotXrefSet() {
        return uniprotXrefSet;
    }

    public void setUniprotXrefSet(Set<UniprotXref> uniprotXrefSet) {
        this.uniprotXrefSet = uniprotXrefSet;
    }

    @XmlTransient
    public Set<EnzymePortalCompound> getEnzymePortalCompoundSet() {
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
        return enzymeCatalyticActivitySet;
    }

    public void setEnzymeCatalyticActivitySet(Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet) {
        this.enzymeCatalyticActivitySet = enzymeCatalyticActivitySet;
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
    public int compareTo(UniprotEntry o) {
        return this.entryType.compareTo(o.getEntryType());

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
        // sb.append(", RelatedSpecies=").append(getRelatedspecies());
        sb.append('}');
        return sb.toString();
    }

    public List<String> getSynonym() {

        List<String> synonym = new ArrayList<>();

        Optional<String> synonymName = Optional.ofNullable(this.getSynonymNames());

        if (synonymName.isPresent()) {

            synonym = Stream.of(synonymName.get().split(";"))
                    //.distinct()
                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(getProteinName().trim())))
                    .collect(Collectors.toList());

        }

        return synonym;
    }

    public List<String> getPdbeaccession() {

        return getPdbCodes(this);
    }

    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();
        e.getUniprotXrefSet()
                .stream()
                .filter(x -> PDB_SOURCE.equalsIgnoreCase(x.getSource()))
                .limit(PDB_CODE_LIMIT)
                .forEach(xref -> pdbcodes.add(xref.getSourceId()));

        return pdbcodes;
    }

    public Boolean getExpEvidence() {
        expEvidence = false;
        if (expEvidenceFlag != null) {
            if (expEvidenceFlag.equals(BigInteger.ONE)) {
                expEvidence = Boolean.TRUE;
            } else {
                expEvidence = Boolean.FALSE;
            }
        }

        return expEvidence;
    }

    public Species getSpecies() {
        species = new Species();
        species.setCommonname(this.getCommonName());
        species.setScientificname(this.getScientificName());
        species.setSelected(false);
        species.setTaxId(this.getTaxId());

        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public List<Compound> getCompounds() {
        compounds = getEnzymePortalCompoundSet().stream().collect(Collectors.toList());
        return compounds;
    }

    public void setCompounds(List<Compound> compounds) {
        this.compounds = compounds;
    }

    public List<Disease> getDiseases() {
        diseases = getEnzymePortalDiseaseSet().stream().collect(Collectors.toList());
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public Boolean getScoring() {
        return scoring;
    }

    public void setScoring(Boolean scoring) {
        this.scoring = scoring;
    }

    public Float getIdentity() {
        return identity;
    }

    public void setIdentity(Float identity) {
        this.identity = identity;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getEnzymeFunction() {
        return getFunction();
    }

    public String getUniprotid() {
        return name;
    }

    public List<String> getUniprotaccessions() {

        return Stream.of(getAccession()).collect(Collectors.toList());

    }

    public List<String> getEc() {

        List<String> ec = new ArrayList<>();
        if (getEnzymePortalEcNumbersSet() != null) {
            this.getEnzymePortalEcNumbersSet()
                    .stream()
                    .forEach(ecNum -> ec.add(ecNum.getEcNumber()));
            return ec;
        }

        return ec;

    }

    public List<EnzymeAccession> getRelatedspecies() {

        final Map<Integer, UniprotEntry> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        LinkedHashSet<EnzymeAccession> relatedspecies = new LinkedHashSet<>();

        if (getRelatedProteinsId() != null) {
            getRelatedProteinsId().getUniprotEntrySet().stream().forEach((entry) -> {

                sortSpecies(entry.getSpecies(), entry, priorityMapper, customKey, key);

                // relatedspecies.add(entry);
            });
        }

        priorityMapper.entrySet().stream().forEach(map -> {
            UniprotEntry protein = map.getValue();
            EnzymeAccession ea = new EnzymeAccession();
            ea.setSpecies(protein.getSpecies());
            ea.setExpEvidence(protein.getExpEvidence());
            ea.getUniprotaccessions().add(protein.getAccession());
            ea.setAccession(protein.getAccession());
            ea.setUniprotid(protein.getName());
            ea.setCompounds(protein.getCompounds());
            ea.setDiseases(protein.getDiseases());
            ea.setEnzymeFunction(protein.getEnzymeFunction());
            ea.setPdbeaccession(protein.getPdbeaccession());
            
            relatedspecies.add(ea);

        });

        List<EnzymeAccession> sortedSpecies = relatedspecies
                .stream()
                .distinct()
                .sorted(Comparator.comparing(EnzymeAccession::getExpEvidence)
                        .reversed())
                .collect(Collectors.toList());

        return sortedSpecies
                .stream()
                //.distinct()
                //.limit(SORTED_SPECIES_LIMIT)
                .collect(Collectors.toList());

    }

    private void sortSpecies(Species sp, UniprotEntry entry, Map<Integer, UniprotEntry> priorityMapper, AtomicInteger customKey, AtomicInteger key) {
        //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli, Rat,worm
        // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
        if (sp.getTaxId().equals(ModelOrganisms.HUMAN.getTaxId())) {

            priorityMapper.put(1, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE.getTaxId())) {

            priorityMapper.put(2, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE_EAR_CRESS.getTaxId())) {

            priorityMapper.put(3, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.FRUIT_FLY.getTaxId())) {

            priorityMapper.put(4, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.ECOLI.getTaxId())) {

            priorityMapper.put(5, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.BAKER_YEAST.getTaxId())) {
            priorityMapper.put(6, entry);

        } else if (sp.getTaxId().equals(ModelOrganisms.RAT.getTaxId())) {
            priorityMapper.put(customKey.getAndIncrement(), entry);
        } else {
            priorityMapper.put(key.getAndIncrement(), entry);
        }
    }

    @XmlTransient
    public Set<EnzymePortalReaction> getEnzymePortalReactionSet() {
        return enzymePortalReactionSet;
    }

    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
        this.enzymePortalReactionSet = enzymePortalReactionSet;
    }

}
