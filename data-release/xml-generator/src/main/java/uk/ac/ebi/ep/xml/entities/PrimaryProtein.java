package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "PRIMARY_PROTEIN")
@XmlRootElement

@NamedQuery(name = "PrimaryProtein.findAll", query = "SELECT p FROM PrimaryProtein p")
@NamedQuery(name = "PrimaryProtein.findByRelatedProteinsId", query = "SELECT p FROM PrimaryProtein p WHERE p.relatedProteinsId = :relatedProteinsId")
@NamedQuery(name = "PrimaryProtein.findByAccession", query = "SELECT p FROM PrimaryProtein p WHERE p.accession = :accession")
@NamedQuery(name = "PrimaryProtein.findByTaxId", query = "SELECT p FROM PrimaryProtein p WHERE p.taxId = :taxId")
@NamedQuery(name = "PrimaryProtein.findByCommonName", query = "SELECT p FROM PrimaryProtein p WHERE p.commonName = :commonName")
@NamedQuery(name = "PrimaryProtein.findByScientificName", query = "SELECT p FROM PrimaryProtein p WHERE p.scientificName = :scientificName")
@NamedQuery(name = "PrimaryProtein.findByPriorityCode", query = "SELECT p FROM PrimaryProtein p WHERE p.priorityCode = :priorityCode")
@NamedQuery(name = "PrimaryProtein.findByPdbFlag", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbFlag = :pdbFlag")
@NamedQuery(name = "PrimaryProtein.findByPdbId", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbId = :pdbId")
@NamedQuery(name = "PrimaryProtein.findByFunction", query = "SELECT p FROM PrimaryProtein p WHERE p.function = :function")
@NamedQuery(name = "PrimaryProtein.findByPdbSpecies", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbSpecies = :pdbSpecies")
@NamedQuery(name = "PrimaryProtein.findByProteinGroupId", query = "SELECT p FROM PrimaryProtein p WHERE p.proteinGroupId = :proteinGroupId")
@NamedQuery(name = "PrimaryProtein.findByEntryType", query = "SELECT p FROM PrimaryProtein p WHERE p.entryType = :entryType")
@NamedQuery(name = "PrimaryProtein.findByPdbLinkedAcc", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbLinkedAcc = :pdbLinkedAcc")
public class PrimaryProtein implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "RELATED_PROTEINS_ID")
    private BigInteger relatedProteinsId;
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "TAX_ID")
    private BigInteger taxId;
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Column(name = "PRIORITY_CODE")
    private String priorityCode;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;
    @Column(name = "PDB_ID")
    private String pdbId;
    @Column(name = "FUNCTION")
    private String function;
    @Column(name = "PDB_SPECIES")
    private String pdbSpecies;
    @Id
    @Basic(optional = false)
    @Column(name = "PROTEIN_GROUP_ID")
    private String proteinGroupId;
    @Column(name = "ENTRY_TYPE")
    private BigInteger entryType;
    @Column(name = "PDB_LINKED_ACC")
    private String pdbLinkedAcc;
    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ProteinGroups proteinGroups;

    public PrimaryProtein() {
}

    public PrimaryProtein(String proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public BigInteger getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(BigInteger relatedProteinsId) {
        this.relatedProteinsId = relatedProteinsId;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public BigInteger getTaxId() {
        return taxId;
    }

    public void setTaxId(BigInteger taxId) {
        this.taxId = taxId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public Character getPdbFlag() {
        return pdbFlag;
    }

    public void setPdbFlag(Character pdbFlag) {
        this.pdbFlag = pdbFlag;
    }

    public String getPdbId() {
        return pdbId;
    }

    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPdbSpecies() {
        return pdbSpecies;
    }

    public void setPdbSpecies(String pdbSpecies) {
        this.pdbSpecies = pdbSpecies;
    }

    public String getProteinGroupId() {
        return proteinGroupId;
    }

    public void setProteinGroupId(String proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public BigInteger getEntryType() {
        return entryType;
    }

    public void setEntryType(BigInteger entryType) {
        this.entryType = entryType;
    }

    public String getPdbLinkedAcc() {
        return pdbLinkedAcc;
    }

    public void setPdbLinkedAcc(String pdbLinkedAcc) {
        this.pdbLinkedAcc = pdbLinkedAcc;
    }

    public ProteinGroups getProteinGroups() {
        return proteinGroups;
    }

    public void setProteinGroups(ProteinGroups proteinGroups) {
        this.proteinGroups = proteinGroups;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proteinGroupId != null ? proteinGroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PrimaryProtein)) {
            return false;
        }
        PrimaryProtein other = (PrimaryProtein) object;
        return !((this.proteinGroupId == null && other.proteinGroupId != null) || (this.proteinGroupId != null && !this.proteinGroupId.equals(other.proteinGroupId)));
    }


    
}
