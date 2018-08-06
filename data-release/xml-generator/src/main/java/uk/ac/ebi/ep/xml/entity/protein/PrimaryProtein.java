
package uk.ac.ebi.ep.xml.entity.protein;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "PRIMARY_PROTEIN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrimaryProtein.findAll", query = "SELECT p FROM PrimaryProtein p"),
    @NamedQuery(name = "PrimaryProtein.findByRelatedProteinsId", query = "SELECT p FROM PrimaryProtein p WHERE p.relatedProteinsId = :relatedProteinsId"),
    @NamedQuery(name = "PrimaryProtein.findByAccession", query = "SELECT p FROM PrimaryProtein p WHERE p.accession = :accession"),
    @NamedQuery(name = "PrimaryProtein.findByTaxId", query = "SELECT p FROM PrimaryProtein p WHERE p.taxId = :taxId"),
    @NamedQuery(name = "PrimaryProtein.findByCommonName", query = "SELECT p FROM PrimaryProtein p WHERE p.commonName = :commonName"),
    @NamedQuery(name = "PrimaryProtein.findByScientificName", query = "SELECT p FROM PrimaryProtein p WHERE p.scientificName = :scientificName"),
    @NamedQuery(name = "PrimaryProtein.findByPriorityCode", query = "SELECT p FROM PrimaryProtein p WHERE p.priorityCode = :priorityCode"),
    @NamedQuery(name = "PrimaryProtein.findByPdbFlag", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbFlag = :pdbFlag"),
    @NamedQuery(name = "PrimaryProtein.findByPdbId", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbId = :pdbId"),
    @NamedQuery(name = "PrimaryProtein.findByFunction", query = "SELECT p FROM PrimaryProtein p WHERE p.function = :function"),
    @NamedQuery(name = "PrimaryProtein.findByPdbSpecies", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbSpecies = :pdbSpecies"),
    @NamedQuery(name = "PrimaryProtein.findByProteinGroupId", query = "SELECT p FROM PrimaryProtein p WHERE p.proteinGroupId = :proteinGroupId"),
    @NamedQuery(name = "PrimaryProtein.findByEntryType", query = "SELECT p FROM PrimaryProtein p WHERE p.entryType = :entryType")})
public class PrimaryProtein implements Serializable {

    @Column(name = "RELATED_PROTEINS_ID")
    private Long relatedProteinsId;
    @Size(max = 15)
    @Column(name = "PDB_LINKED_ACC")
    private String pdbLinkedAcc;
    private static final long serialVersionUID = 1L;
    @Size(max = 15)
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "TAX_ID")
    private BigInteger taxId;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 3)
    @Column(name = "PRIORITY_CODE")
    private String priorityCode;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;
    @Size(max = 10)
    @Column(name = "PDB_ID")
    private String pdbId;
    @Size(max = 4000)
    @Column(name = "FUNCTION")
    private String function;
    @Size(max = 255)
    @Column(name = "PDB_SPECIES")
    private String pdbSpecies;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PROTEIN_GROUP_ID")
    private String proteinGroupId;
    @Column(name = "ENTRY_TYPE")
    private BigInteger entryType;
    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ProteinGroups proteinGroups;

    public PrimaryProtein() {
    }

    public PrimaryProtein(String proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public Long getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(Long relatedProteinsId) {
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrimaryProtein)) {
            return false;
        }
        PrimaryProtein other = (PrimaryProtein) object;
        if ((this.proteinGroupId == null && other.proteinGroupId != null) || (this.proteinGroupId != null && !this.proteinGroupId.equals(other.proteinGroupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.model.PrimaryProtein[ proteinGroupId=" + proteinGroupId + " ]";
    }

    public String getPdbLinkedAcc() {
        return pdbLinkedAcc;
    }

    public void setPdbLinkedAcc(String pdbLinkedAcc) {
        this.pdbLinkedAcc = pdbLinkedAcc;
    }
    
}
