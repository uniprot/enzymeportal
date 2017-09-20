package uk.ac.ebi.ep.model;

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
import org.springframework.util.StringUtils;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "PRIMARY_PROTEIN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrimaryProtein.findAll", query = "SELECT p FROM PrimaryProtein p"),
    @NamedQuery(name = "PrimaryProtein.findByProteinGroupId", query = "SELECT p FROM PrimaryProtein p WHERE p.proteinGroupId = :proteinGroupId"),
    @NamedQuery(name = "PrimaryProtein.findByAccession", query = "SELECT p FROM PrimaryProtein p WHERE p.accession = :accession"),
    @NamedQuery(name = "PrimaryProtein.findByTaxId", query = "SELECT p FROM PrimaryProtein p WHERE p.taxId = :taxId"),
    @NamedQuery(name = "PrimaryProtein.findByCommonName", query = "SELECT p FROM PrimaryProtein p WHERE p.commonName = :commonName"),
    @NamedQuery(name = "PrimaryProtein.findByScientificName", query = "SELECT p FROM PrimaryProtein p WHERE p.scientificName = :scientificName"),
    @NamedQuery(name = "PrimaryProtein.findByPriorityCode", query = "SELECT p FROM PrimaryProtein p WHERE p.priorityCode = :priorityCode")})
public class PrimaryProtein implements Serializable {
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;
    @Size(max = 10)
    @Column(name = "PDB_ID")
    private String pdbId;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PROTEIN_GROUP_ID")
    private String proteinGroupId;
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
    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ProteinGroups proteinGroups;


    public PrimaryProtein() {
    }

    public PrimaryProtein(String proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public String getProteinGroupId() {
        return proteinGroupId;
    }

    public void setProteinGroupsId(String proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
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
        if (commonName == null || StringUtils.isEmpty(commonName)) {
            commonName = scientificName;
        }
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

    @Override
    public String toString() {
        return "PrimaryProtein{" + "proteinGroupsId=" + proteinGroupId + ", accession=" + accession + ", taxId=" + taxId + ", commonName=" + commonName + ", scientificName=" + scientificName + ", priorityCode=" + priorityCode + '}';
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

}
