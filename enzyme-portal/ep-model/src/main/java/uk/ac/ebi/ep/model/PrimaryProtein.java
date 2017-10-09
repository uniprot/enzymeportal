package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @NamedQuery(name = "PrimaryProtein.findByPdbSpecies", query = "SELECT p FROM PrimaryProtein p WHERE p.pdbSpecies = :pdbSpecies")})
public class PrimaryProtein implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RELATED_PROTEINS_ID")
    private BigDecimal relatedProteinsId;
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
    @JoinColumn(name = "RELATED_PROTEINS_ID", referencedColumnName = "REL_PROT_INTERNAL_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private RelatedProteins relatedProteins;

    public PrimaryProtein() {
    }

    public PrimaryProtein(BigDecimal relatedProteinsId) {
        this.relatedProteinsId = relatedProteinsId;
    }

    public BigDecimal getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(BigDecimal relatedProteinsId) {
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

    public RelatedProteins getRelatedProteins() {
        return relatedProteins;
    }

    public void setRelatedProteins(RelatedProteins relatedProteins) {
        this.relatedProteins = relatedProteins;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relatedProteinsId != null ? relatedProteinsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof PrimaryProtein)) {
            return false;
        }
        PrimaryProtein other = (PrimaryProtein) object;
        return !((this.relatedProteinsId == null && other.relatedProteinsId != null) || (this.relatedProteinsId != null && !this.relatedProteinsId.equals(other.relatedProteinsId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.model.PrimaryProtein[ relatedProteinsId=" + relatedProteinsId + " ]";
    }
    
}
