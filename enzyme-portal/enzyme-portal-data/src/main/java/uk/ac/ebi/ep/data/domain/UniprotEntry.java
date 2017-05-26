package uk.ac.ebi.ep.data.domain;

import com.mysema.query.annotations.QueryInit;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.data.common.ModelOrganisms;
import uk.ac.ebi.ep.data.entry.AssociatedProtein;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement

@NamedEntityGraph(name = "UniprotEntryEntityGraph", attributeNodes = {
    @NamedAttributeNode(value = "relatedProteinsId", subgraph = "uniprotEntrySet"),
    @NamedAttributeNode("enzymePortalPathwaysSet"),
    @NamedAttributeNode("enzymePortalReactionSet"),
    @NamedAttributeNode("enzymePortalSummarySet"),
    @NamedAttributeNode(value = "enzymePortalCompoundSet", subgraph = "enzymePortalCompoundSet"),
    @NamedAttributeNode("enzymePortalDiseaseSet"),
    @NamedAttributeNode("uniprotXrefSet"),
    @NamedAttributeNode("enzymePortalEcNumbersSet"),
    @NamedAttributeNode("enzymeCatalyticActivitySet")
},
        subgraphs = {
            @NamedSubgraph(
                    name = "relatedProteinsId",
                    attributeNodes = {
                        @NamedAttributeNode(value = "uniprotEntrySet", subgraph = "enzymePortalCompoundSet")}
            )
        }
)
@NamedQueries({
    @NamedQuery(name = "UniprotEntry.findAll", query = "SELECT u FROM UniprotEntry u"),
    @NamedQuery(name = "UniprotEntry.findByDbentryId", query = "SELECT u FROM UniprotEntry u WHERE u.dbentryId = :dbentryId"),
    @NamedQuery(name = "UniprotEntry.findByProteinName", query = "SELECT u FROM UniprotEntry u WHERE u.proteinName = :proteinName"),
    @NamedQuery(name = "UniprotEntry.findByScientificName", query = "SELECT u FROM UniprotEntry u WHERE u.scientificName = :scientificName"),
    @NamedQuery(name = "UniprotEntry.findByCommonName", query = "SELECT u FROM UniprotEntry u WHERE u.commonName = :commonName")

})

//@SqlResultSetMapping(
//        name = "browseTaxonomy",
//        classes = {
//            @ConstructorResult(
//                    targetClass = Taxonomy.class,
//                    columns = {
//                        @ColumnResult(name = "tax_Id", type = Long.class),
//                        @ColumnResult(name = "scientific_Name"),
//                        @ColumnResult(name = "common_Name"),
//                        @ColumnResult(name = "numEnzymes", type = Long.class)
//                    }
//            )
//        }
//)
@SqlResultSetMappings({
    @SqlResultSetMapping(
            name = "browseTaxonomy",
            classes = {
                @ConstructorResult(
                        targetClass = Taxonomy.class,
                        columns = {
                            @ColumnResult(name = "tax_Id", type = Long.class),
                            @ColumnResult(name = "scientific_Name"),
                            @ColumnResult(name = "common_Name"),
                            @ColumnResult(name = "numEnzymes", type = Long.class)
                        }
                )
            }
    ),
    @SqlResultSetMapping(
            name = "associatedProteins",
            classes = {
                @ConstructorResult(
                        targetClass = AssociatedProtein.class,
                        columns = {
                            @ColumnResult(name = "PROTEIN_NAME"),
                            @ColumnResult(name = "ACCESSION"),
                            @ColumnResult(name = "COMMON_NAME")

                        }
                )
            }
    )

})

public class UniprotEntry extends EnzymeAccession implements Serializable, Comparable<UniprotEntry> {

    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProteinGroups proteinGroupId;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;

    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EntryToGeneMapping> entryToGeneMappingSet;

    @Column(name = "FUNCTION_LENGTH")
    private BigInteger functionLength;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;

    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet;

    private static final long serialVersionUID = 1L;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalNames> enzymePortalNamesSet;
    @OneToMany(mappedBy = "accession", fetch = FetchType.LAZY)
    private Set<EnzymeXmlStore> enzymeXmlStoreSet;

    @Column(name = "ENTRY_TYPE")
    private Short entryType;
    @Column(name = "FUNCTION")
    private String function;
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTimestamp;

    @JoinColumn(name = "RELATED_PROTEINS_ID", referencedColumnName = "REL_PROT_INTERNAL_ID")
    //@ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne
    //@BatchSize(size = 10)
    //@Fetch(FetchMode.JOIN)
    private RelatedProteins relatedProteinsId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    //@BatchSize(size = 10)
    @Fetch(FetchMode.JOIN)
    @QueryInit("enzymePortalEcNumbersSet")
    private Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet;
    @Column(name = "SEQUENCE_LENGTH")
    private Integer sequenceLength;

    //@Lob
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;

    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Id
    @Basic(optional = false)
    @Column(name = "ACCESSION", unique = true)
    private String accession;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TAX_ID")
    private Long taxId;
    @Column(name = "PROTEIN_NAME", unique = true)
    private String proteinName;
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Column(name = "COMMON_NAME")
    private String commonName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<UniprotXref> uniprotXrefSet;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalPathways> enzymePortalPathwaysSet;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalReaction> enzymePortalReactionSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private List<EnzymePortalSummary> enzymePortalSummarySet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    //@BatchSize(size = 10)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    //@BatchSize(size = 20)
    @Fetch(FetchMode.JOIN)
    private Set<EnzymePortalDisease> enzymePortalDiseaseSet;

    public UniprotEntry() {
    }

    public UniprotEntry(String accession) {
        Assert.notNull(accession, "Accession must not be null");
        this.accession = accession;
    }

    public UniprotEntry(String accession, long dbentryId) {
        this.accession = accession;
        this.dbentryId = dbentryId;
    }

    public UniprotEntry(String accession, String name, String proteinName, String scientificName, String commonName) {

        this.accession = accession;
        this.name = name;
        this.proteinName = proteinName;
        this.scientificName = scientificName;
        this.commonName = commonName;

    }

    public UniprotEntry(String accession, Set<EnzymePortalDisease> enzymePortalDiseaseSet) {
        this.accession = accession;
        this.enzymePortalDiseaseSet = enzymePortalDiseaseSet;
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

    @Override
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

    @XmlTransient
    public Set<EnzymePortalDisease> getEnzymePortalDiseaseSet() {
        return enzymePortalDiseaseSet;
    }

    public void setEnzymePortalDiseaseSet(Set<EnzymePortalDisease> enzymePortalDiseaseSet) {
        this.enzymePortalDiseaseSet = enzymePortalDiseaseSet;
    }

    @XmlTransient
    public Set<EnzymePortalReaction> getEnzymePortalReactionSet() {
        return enzymePortalReactionSet;
    }

    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
        this.enzymePortalReactionSet = enzymePortalReactionSet;
    }

    @XmlTransient
    public List<EnzymePortalSummary> getEnzymePortalSummarySet() {
        return enzymePortalSummarySet;
    }

    public void setEnzymePortalSummarySet(List<EnzymePortalSummary> enzymePortalSummarySet) {
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
    public Set<UniprotXref> getUniprotXrefSet() {
        return uniprotXrefSet;
    }

    public void setUniprotXrefSet(Set<UniprotXref> uniprotXrefSet) {
        this.uniprotXrefSet = uniprotXrefSet;
    }

    @XmlTransient
    public Set<EnzymePortalPathways> getEnzymePortalPathwaysSet() {
        return enzymePortalPathwaysSet;
    }

    public void setEnzymePortalPathwaysSet(Set<EnzymePortalPathways> enzymePortalPathwaysSet) {
        this.enzymePortalPathwaysSet = enzymePortalPathwaysSet;
    }

    public String getSynonymNames() {
        return synonymNames;
    }

    public void setSynonymNames(String synonymNames) {
        this.synonymNames = synonymNames;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.proteinName);
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
        final UniprotEntry other = (UniprotEntry) obj;
        if (!Objects.equals(this.proteinName, other.proteinName)) {
            return false;
        }
        return true;
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 59 * hash + Objects.hashCode(this.accession);
//        hash = 59 * hash + Objects.hashCode(this.proteinName);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final UniprotEntry other = (UniprotEntry) obj;
//        if (!Objects.equals(this.accession, other.accession)) {
//            return false;
//        }
//        return Objects.equals(this.proteinName, other.proteinName);
//    }
    public String getScientificname() {
        return getScientificName();
    }

    public String getCommonname() {
        return getCommonName();
    }

    @Override
    public Species getSpecies() {
        Species specie = new Species();
        specie.setCommonname(this.getCommonName());
        specie.setScientificname(this.getScientificName());
        specie.setSelected(false);
        specie.setTaxId(this.getTaxId());

        return specie;
    }

    public Integer getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(Integer sequenceLength) {
        this.sequenceLength = sequenceLength;
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

    public RelatedProteins getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(RelatedProteins relatedProteinsId) {
        this.relatedProteinsId = relatedProteinsId;
    }

    @Override
    public List<String> getPdbeaccession() {

        return getPdbCodes(this);
    }

    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();

        e.getUniprotXrefSet().stream().filter(x -> PDB_SOURCE.equalsIgnoreCase(x.getSource())).limit(PDB_CODE_LIMIT).collect(Collectors.toList()).stream().forEach(xref -> {
            pdbcodes.add(xref.getSourceId());
        });

        return pdbcodes.stream().sorted().collect(Collectors.toList());

    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public List<String> getSynonym() {

        List<String> synonym = new ArrayList<>();

        Optional<String> synonymName = Optional.ofNullable(this.getSynonymNames());

        if (synonymName.isPresent()) {

            synonym = Stream.of(synonymName.get().split(";"))
                    .distinct()
                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(getProteinName().trim())))
                    .distinct()
                    .collect(Collectors.toList());

        }

        return synonym;
    }

    public List<String> parseNameSynonyms(String namesColumn) {
        List<String> nameSynonyms = new ArrayList<>();
        if (namesColumn != null) {
            final int sepIndex = namesColumn.indexOf(" (");

            if (sepIndex == -1) {
                // no synonyms, just recommended name:

                nameSynonyms.add(namesColumn);
            } else {
                // Recommended name:
                nameSynonyms.add(namesColumn.substring(0, sepIndex));
                // take out starting and ending parentheses
                String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
                nameSynonyms.addAll(Arrays.asList(synonyms));
            }
            return nameSynonyms.stream().distinct().collect(Collectors.toList());
        }
        return nameSynonyms;
    }

    public List<String> getEc() {

        List<String> ec = new ArrayList<>();
        if (!getEnzymePortalEcNumbersSet().isEmpty()) {
            this.getEnzymePortalEcNumbersSet()
                    .stream()
                    .forEach(ecNum -> ec.add(ecNum.getEcNumber()));
        }

        return ec;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public List<Compound> getCompounds() {

        if (this.getEnzymePortalCompoundSet() != null) {
            return this.getEnzymePortalCompoundSet()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public List<Disease> getDiseases() {
        if (this.getEnzymePortalDiseaseSet() != null) {

            return this.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<EnzymeAccession> getRelatedspecies() {
        return getRelatedProteinsId().getUniprotEntrySet()
                .stream()
                .sorted(Comparator.comparing(EnzymeAccession::getExpEvidence)
                        .reversed())
                //.limit(SORTED_SPECIES_LIMIT)
                .collect(Collectors.toList());
    }

    public List<EnzymeAccession> getRelatedspecies1() {

        final Map<Integer, UniprotEntry> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        LinkedHashSet<EnzymeAccession> relatedspecies = new LinkedHashSet<>();

        if (getRelatedProteinsId() != null) {

            getRelatedProteinsId().getUniprotEntrySet().stream().forEach((entry) -> {

                Species sp = buildSpeciesFromEntry(entry);
                entry.setSpecies(sp);

                Optional<BigInteger> evidence = Optional.ofNullable(entry.getExpEvidenceFlag());

                if (evidence.isPresent()) {
                    if (evidence.get().equals(BigInteger.ONE)) {
                        entry.setExpEvidence(Boolean.TRUE);
                    } else {
                        entry.setExpEvidence(Boolean.FALSE);
                    }
                }

                sortSpecies(sp, entry, priorityMapper, customKey, key);

                // relatedspecies.add(entry);
            });
        }

        priorityMapper.entrySet().stream().forEach(map -> {
            relatedspecies.add(map.getValue());
        });

        List<EnzymeAccession> sortedSpecies = relatedspecies
                .stream()
                //.distinct()
                .sorted(Comparator.comparing(EnzymeAccession::getExpEvidence)
                        .reversed())
                .collect(Collectors.toList());

        return sortedSpecies
                .stream()
                //.distinct()
                .limit(SORTED_SPECIES_LIMIT)
                .collect(Collectors.toList());

    }

    private Species buildSpeciesFromEntry(UniprotEntry entry) {
        Species sp = new Species();
        sp.setScientificname(entry.getScientificName());
        sp.setCommonname(entry.getCommonName());
        sp.setSelected(false);
        sp.setTaxId(entry.getTaxId());
        return sp;
    }

    public Boolean humanOnTop() {
        return this.getTaxId().equals(ModelOrganisms.HUMAN.getTaxId());
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

    @Override
    public int compareTo(UniprotEntry o) {
        return this.entryType.compareTo(o.getEntryType());

    }

    @Override
    public String getUniprotid() {
        return name;
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

    @Override
    public List<String> getUniprotaccessions() {
        //return Collections.singletonList(getAccession());
        return Stream.of(getAccession()).collect(Collectors.toList());

    }

    @XmlTransient
    public Set<EnzymePortalNames> getEnzymePortalNamesSet() {
        return enzymePortalNamesSet;
    }

    public void setEnzymePortalNamesSet(Set<EnzymePortalNames> enzymePortalNamesSet) {
        this.enzymePortalNamesSet = enzymePortalNamesSet;
    }

    @XmlTransient
    public Set<EnzymeXmlStore> getEnzymeXmlStoreSet() {
        return enzymeXmlStoreSet;
    }

    public void setEnzymeXmlStoreSet(Set<EnzymeXmlStore> enzymeXmlStoreSet) {
        this.enzymeXmlStoreSet = enzymeXmlStoreSet;
    }

    @XmlTransient
    public Set<EnzymeCatalyticActivity> getEnzymeCatalyticActivitySet() {
        return enzymeCatalyticActivitySet;
    }

    public void setEnzymeCatalyticActivitySet(Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet) {
        this.enzymeCatalyticActivitySet = enzymeCatalyticActivitySet;
    }

    @Override
    public Boolean getScoring() {
        return scoring;
    }

    @Override
    public void setScoring(Boolean value) {
        this.scoring = value;
    }

    public BigInteger getFunctionLength() {
        return functionLength;
    }

    public void setFunctionLength(BigInteger functionLength) {
        this.functionLength = functionLength;
    }

    public BigInteger getExpEvidenceFlag() {
        if (expEvidenceFlag != null) {
            if (expEvidenceFlag.equals(BigInteger.ONE)) {
                this.setExpEvidence(Boolean.TRUE);
            } else {
                this.setExpEvidence(Boolean.FALSE);
            }
        }

        return expEvidenceFlag;
    }

    public void setExpEvidenceFlag(BigInteger expEvidenceFlag) {
        this.expEvidenceFlag = expEvidenceFlag;
    }

    @Override
    public String getUniprotaccession() {
        return this.getAccession();
    }

    @Override
    public String getEnzymeFunction() {
        return getFunction();
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

    @XmlTransient
    public Set<EntryToGeneMapping> getEntryToGeneMappingSet() {
        return entryToGeneMappingSet;
    }

    public void setEntryToGeneMappingSet(Set<EntryToGeneMapping> entryToGeneMappingSet) {
        this.entryToGeneMappingSet = entryToGeneMappingSet;
    }

    @Override
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

    public Character getPdbFlag() {
        return pdbFlag;
    }

    public void setPdbFlag(Character pdbFlag) {
        this.pdbFlag = pdbFlag;
    }

    public ProteinGroups getProteinGroupId() {
        return proteinGroupId;
    }

    public void setProteinGroupId(ProteinGroups proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

}
