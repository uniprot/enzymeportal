/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
)

public class UniprotEntry extends EnzymeAccession implements Serializable, Comparable<UniprotEntry> {

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
    @ManyToOne(fetch = FetchType.LAZY)
    //@BatchSize(size = 10)
    @Fetch(FetchMode.JOIN)
    private RelatedProteins relatedProteinsId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    //@BatchSize(size = 10)
    @Fetch(FetchMode.JOIN)
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

    //Note : using protein name will reduce all related species to 1 per enzyme hence we use uniprot accession for the equals and hashcode
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.accession);
        hash = 97 * hash + Objects.hashCode(this.proteinName);
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
        if (!Objects.equals(this.accession, other.accession)) {
            return false;
        }
        return Objects.equals(this.proteinName, other.proteinName);
    }

    public String getScientificname() {
        return getScientificName();
    }

    public String getCommonname() {
        return getCommonName();
    }

//
//    @Override
//    public int compareTo(UniprotEntry other) {
//        if (this.getCommonname() == null & other.getCommonname() == null) {
//            return this.getScientificname().compareTo(other.getScientificname());
//        }
//        if (this.getCommonname() != null & other.getCommonname() == null) {
//            return this.getCommonname().compareTo(other.getScientificname());
//        }
//        if (this.getCommonname() == null & other.getCommonname() != null) {
//            return this.getScientificname().compareTo(other.getCommonname());
//        }
//
//        if (this.getCommonname() != null & this.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName()) && other.getCommonname() != null & other.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName())) {
//            return this.getScientificname().compareTo(other.getScientificname());
//        }
//        return this.getCommonname().compareTo(other.getCommonname());
//
//    }
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

        e.getUniprotXrefSet().stream().filter(x -> "PDB".equalsIgnoreCase(x.getSource())).limit(500).collect(Collectors.toList()).stream().forEach(xref -> {
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
            List<String> syn = Arrays.asList(synonymName.get().split("^[,;]+$"));

            for (String otherName : syn) {
                if (!otherName.trim().equalsIgnoreCase(getProteinName().trim())) {
                    synonym.add(otherName);
                }
            }
        }

        return synonym.stream().distinct().collect(Collectors.toList());
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
            this.getEnzymePortalEcNumbersSet().stream().forEach(ecNum -> {
                ec.add(ecNum.getEcNumber());
            });
        }

        return ec;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public List<Compound> getCompounds() {

        if (this.getEnzymePortalCompoundSet() != null) {
            return this.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList());
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

        //getSpecies();
        LinkedList<EnzymeAccession> relatedspecies = new LinkedList<>();
        //String defaultSpecies = CommonSpecies.Human.getScientificName();

        if (this.getRelatedProteinsId() != null) {
            //current specie is the default specie
            this.getRelatedProteinsId().getUniprotEntrySet().stream().forEach((entry) -> {
//
//              if (!entry.getPdbeaccession().isEmpty()) {
//                  relatedspecies.push(entry);
//              }else{
//                 relatedspecies.offer(entry); 
//              }   
//                
                
//            if(entry.getIdentity() > 0.0f){
//                relatedspecies.add(entry); 
//            }else{
            if (entry.getScientificName() != null && entry.getScientificName().equalsIgnoreCase(getSpecies().getScientificname())) {
                    entry.getUniprotaccessions().add(getAccession());
                        relatedspecies.add(0, entry);

            } else if (entry.getScientificName() != null && !entry.getScientificName().equalsIgnoreCase(getSpecies().getScientificname())) {
                relatedspecies.add(entry);

                    }
                //}

            });
        }

        //human is default specie
//        this.getRelatedProteinsId().getUniprotEntrySet().stream().forEach((entry) -> {
//            if (entry.getScientificName() != null && entry.getScientificName().equalsIgnoreCase(defaultSpecies)) {
//
//                relatedspecies.add(0, entry);
//
//            } else if (entry.getScientificName() != null && !entry.getScientificName().equalsIgnoreCase(defaultSpecies)) {
//                relatedspecies.add(entry);
//
//            }
//
//        });
        // List<EnzymeAccession> relSpecies = relatedspecies.stream().distinct().limit(50).collect(Collectors.toList());
        //Collections.sort(relSpecies, (id1, id2) -> -(Float.compare(id1.getIdentity(), id2.getIdentity())));
        //return relatedspecies.stream().sorted((id1, id2) -> -(Float.compare(id1.getIdentity(), id2.getIdentity()))).collect(Collectors.toList());
        return relatedspecies.stream().distinct().limit(50).collect(Collectors.toList());
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

        List<String> uniprotAccessions = new ArrayList<>();

        uniprotAccessions.add(getAccession());
        return uniprotAccessions.stream().distinct().collect(Collectors.toList());
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

}
