/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.search.model.Species;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement
@NamedEntityGraph(name = "graph.UniprotEntry",
        attributeNodes = @NamedAttributeNode("enzymePortalPathwaysSet")
)
@NamedQueries({
    @NamedQuery(name = "UniprotEntry.findAll", query = "SELECT u FROM UniprotEntry u"),
    @NamedQuery(name = "UniprotEntry.findByDbentryId", query = "SELECT u FROM UniprotEntry u WHERE u.dbentryId = :dbentryId"),
    //@NamedQuery(name = "UniprotEntry.findByAccession", query = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession"),
    //@NamedQuery(name = "UniprotEntry.findByName", query = "SELECT u FROM UniprotEntry u WHERE u.name = :name"),
    //@NamedQuery(name = "UniprotEntry.findByTaxId", query = "SELECT u FROM UniprotEntry u WHERE u.taxId = :taxId"),
    @NamedQuery(name = "UniprotEntry.findByProteinName", query = "SELECT u FROM UniprotEntry u WHERE u.proteinName = :proteinName"),
    @NamedQuery(name = "UniprotEntry.findByScientificName", query = "SELECT u FROM UniprotEntry u WHERE u.scientificName = :scientificName"),
    @NamedQuery(name = "UniprotEntry.findByCommonName", query = "SELECT u FROM UniprotEntry u WHERE u.commonName = :commonName"),
    @NamedQuery(name = "UniprotEntry.findBySynonymName", query = "SELECT u FROM UniprotEntry u WHERE u.synonymName = :synonymName")})

public class UniprotEntry extends Species implements Serializable, Comparable<UniprotEntry> {

    @Lob
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;

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
    @Column(name = "SYNONYM_NAME")
    private String synonymName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession", fetch = FetchType.LAZY)
    private Set<UniprotXref> uniprotXrefSet;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalPathways> enzymePortalPathwaysSet;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalReaction> enzymePortalReactionSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalSummary> enzymePortalSummarySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalCompound> enzymePortalCompoundSet;
    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalDisease> enzymePortalDiseaseSet;
    
    // private boolean selected;

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

    public String getSynonymName() {
        return synonymName;
    }

    public void setSynonymName(String synonymName) {
        this.synonymName = synonymName;
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
    
    //Note : using protein name will reduce all related species to 1 per enzyme hence we use uniprot name for the equals and hashcode

//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 89 * hash + Objects.hashCode(this.name);
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
//        if (!Objects.equals(this.name, other.name)) {
//            return false;
//        }
//        return true;
//    }
//
//    
    
    

    @Override
    public String toString() {
        return "UniprotEntry{" + "Has synonymNames=" + !StringUtils.isEmpty(synonymNames) + ", accession=" + accession + ", name=" + name + ", proteinName=" + proteinName + ", scientificName=" + scientificName + ", commonName=" + commonName + ", uniprotXrefSet=" + uniprotXrefSet.size() + ", enzymePortalPathwaysSet=" + enzymePortalPathwaysSet.size() + ", enzymePortalReactionSet=" + enzymePortalReactionSet.size() + ", enzymePortalCompoundSet=" + enzymePortalCompoundSet.size() + ", enzymePortalDiseaseSet=" + enzymePortalDiseaseSet.size() + '}';
    }
    
    
    @Override
       public String getScientificname() {
        return scientificName;
    }

 

    @Override
    public String getCommonname() {
        return commonName;
    }

    @Override
    public boolean isSelected() {
        return super.isSelected();
    }

    @Override
    public int compareTo(UniprotEntry other) {
            if (this.getCommonname() == null & other.getCommonname() == null) {
            return this.getScientificname().compareTo(other.getScientificname());
        }
        if (this.getCommonname() != null & other.getCommonname() == null) {
            return this.getCommonname().compareTo(other.getScientificname());
        }
        if (this.getCommonname() == null & other.getCommonname() != null) {
            return this.getScientificname().compareTo(other.getCommonname());
        }

        if (this.getCommonname() != null & this.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName()) && other.getCommonname() != null & other.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName())) {
            return this.getScientificname().compareTo(other.getScientificname());
        }
        return this.getCommonname().compareTo(other.getCommonname());
    
    }





}
