package uk.ac.ebi.ep.xml.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 *
 * @author Joseph
 */
@MappedSuperclass
@Data
public class Protein {

    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    protected long dbentryId;
    @Id
    @Basic(optional = false)
    @Column(name = "ACCESSION")
    protected String accession;
    @Column(name = "NAME")
    protected String name;
    @Column(name = "TAX_ID")
    protected Long taxId;
    @Column(name = "PROTEIN_NAME")
    protected String proteinName;
    @Column(name = "SCIENTIFIC_NAME")
    protected String scientificName;
    @Column(name = "COMMON_NAME")
    protected String commonName;
    @Column(name = "SEQUENCE_LENGTH")
    protected Integer sequenceLength;
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateTimestamp;
    @Column(name = "FUNCTION")
    protected String function;
    @Column(name = "ENTRY_TYPE")
    protected Short entryType;
    @Column(name = "FUNCTION_LENGTH")
    protected BigInteger functionLength;
    @Column(name = "SYNONYM_NAMES")
    protected String synonymNames;
    @Column(name = "EXP_EVIDENCE_FLAG")
    protected BigInteger expEvidenceFlag;
    @Column(name = "UNCHARACTERIZED")
    protected BigInteger uncharacterized;
    @Column(name = "PDB_FLAG")
    protected Character pdbFlag;
//    @OneToMany(mappedBy = "uniprotAccession")
//    //@Fetch(FetchMode.JOIN)
//    protected Set<EntryToGeneMapping> entryToGeneMappingSet = new HashSet<>();
//
//    @Fetch(FetchMode.JOIN)
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
//    protected Set<EnzymePortalDisease> enzymePortalDiseaseSet;
//
//    @Fetch(FetchMode.JOIN)
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession", fetch = FetchType.LAZY)
//    protected Set<UniprotXref> uniprotXrefSet;
//    @Fetch(FetchMode.JOIN)
//    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
//    protected Set<EnzymePortalCompound> enzymePortalCompoundSet;
//    @Fetch(FetchMode.JOIN)
//    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
//    protected Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet;
//    @Fetch(FetchMode.JOIN)
//    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
//    protected Set<EnzymePortalPathways> enzymePortalPathwaysSet;
//    @Fetch(FetchMode.JOIN)
//    @OneToMany(mappedBy = "accession")
//    protected Set<UniprotFamilies> uniprotFamiliesSet;
//    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
//    protected Set<EnzymePortalReaction> enzymePortalReactionSet;
//
//    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
//    protected Set<EnzymePortalReactant> enzymePortalReactantSet;
    
    
        @OneToMany(mappedBy = "uniprotAccession")
    //@Fetch(FetchMode.JOIN)
    protected Set<EntryToGeneMapping> entryToGeneMappingSet = new HashSet<>();

    //@Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uniprotAccession")
    protected Set<EnzymePortalDisease> enzymePortalDiseaseSet = new HashSet<>();

    //@Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accession")
    protected Set<UniprotXref> uniprotXrefSet = new HashSet<>();
    //@Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "uniprotAccession")
    protected Set<EnzymePortalCompound> enzymePortalCompoundSet = new HashSet<>();
   // @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "uniprotAccession")
    protected Set<EnzymeCatalyticActivity> enzymeCatalyticActivitySet = new HashSet<>();
    //@Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "uniprotAccession")
    protected Set<EnzymePortalPathways> enzymePortalPathwaysSet = new HashSet<>();
    //@Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "accession")
    protected Set<UniprotFamilies> uniprotFamiliesSet = new HashSet<>();
    @OneToMany(mappedBy = "uniprotAccession")
    //@Fetch(FetchMode.JOIN)
    protected Set<EnzymePortalReaction> enzymePortalReactionSet = new HashSet<>();

    @OneToMany(mappedBy = "uniprotAccession")
    //@Fetch(FetchMode.JOIN)
    protected Set<EnzymePortalReactant> enzymePortalReactantSet = new HashSet<>();
    
    public String getCommonName() {
        if (commonName == null || StringUtils.isEmpty(commonName)) {
            commonName = scientificName;
        }
        return commonName;
    }

}
