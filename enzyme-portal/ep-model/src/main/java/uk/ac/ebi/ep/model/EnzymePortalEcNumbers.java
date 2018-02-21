package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.model.search.model.EcNumber;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement

@NamedEntityGraph(name = "EcNumberEntityGraph", attributeNodes = {
    @NamedAttributeNode("uniprotAccession")
})
@NamedQueries({
    @NamedQuery(name = "EnzymePortalEcNumbers.findAll", query = "SELECT e FROM EnzymePortalEcNumbers e"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcInternalId", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecInternalId = :ecInternalId")
    //@NamedQuery(name = "EnzymePortalEcNumbers.findByEcNumber", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecNumber = :ecNumber")
})
//public class EnzymePortalEcNumbers extends EcNumber implements Serializable, Comparable<EcNumber> {
public class EnzymePortalEcNumbers implements Serializable, Comparator<EnzymePortalEcNumbers> {

    @Column(name = "EC_FAMILY")
    private Integer ecFamily;
    @Size(max = 4000)
    @Column(name = "COFACTOR")
    private String cofactor;
    @Size(max = 300)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "EC_INTERNAL_ID")
    private BigDecimal ecInternalId;
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
     @ManyToOne
    //@ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    public EnzymePortalEcNumbers() {
    }

    public EnzymePortalEcNumbers(BigDecimal ecInternalId) {
        this.ecInternalId = ecInternalId;
    }

    public BigDecimal getEcInternalId() {
        return ecInternalId;
    }

    public void setEcInternalId(BigDecimal ecInternalId) {
        this.ecInternalId = ecInternalId;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.ecNumber);
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
        final EnzymePortalEcNumbers other = (EnzymePortalEcNumbers) obj;
        return Objects.equals(this.ecNumber, other.ecNumber);
    }

    @Override
    public String toString() {
        return ecNumber;
    }

    /**
     *
     * @return the enzyme family representation of the ec class
     */
    //@Override
    public String getFamily() {
        Optional<Integer> ec = Optional.ofNullable(this.getEcFamily());
        if (ec.isPresent()) {
            return computeEcToFamilyName(ec.get());
        }
        return "";

    }

    /**
     *
     * @return ec class
     */
    //@Override
//    public Integer getEc() {
//        return getEcFamily();
//
//    }

    public Integer getEcFamily() {
        return ecFamily;
    }

    public void setEcFamily(Integer ecFamily) {
        this.ecFamily = ecFamily;
    }

    public String getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    public String getCatalyticActivity() {
        return catalyticActivity;
    }

    public void setCatalyticActivity(String catalyticActivity) {
        this.catalyticActivity = catalyticActivity;
    }

    public Character getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(Character transferFlag) {
        this.transferFlag = transferFlag;
    }

    @Override
    public int compare(EnzymePortalEcNumbers ec1, EnzymePortalEcNumbers ec2) {

        return ec1.getEcFamily().compareTo(ec2.getEcFamily());
    }

    public String computeEcToFamilyName(int ec) {

        if (ec == 1) {

            return EcNumber.EnzymeFamily.OXIDOREDUCTASES.getName();
        }
        if (ec == 2) {
            return EcNumber.EnzymeFamily.TRANSFERASES.getName();
        }
        if (ec == 3) {
            return EcNumber.EnzymeFamily.HYDROLASES.getName();
        }
        if (ec == 4) {
            return EcNumber.EnzymeFamily.LYASES.getName();
        }
        if (ec == 5) {
            return EcNumber.EnzymeFamily.ISOMERASES.getName();
        }
        if (ec == 6) {
            return EcNumber.EnzymeFamily.LIGASES.getName();
        }

        return "Invalid Ec Number";
    }


    public String getCofactor() {
        return cofactor;
    }

    public void setCofactor(String cofactor) {
        this.cofactor = cofactor;
    }

}
