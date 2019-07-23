/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalEcNumbers.findAll", query = "SELECT e FROM EnzymePortalEcNumbers e"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcInternalId", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecInternalId = :ecInternalId"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcFamily", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecFamily = :ecFamily"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEnzymeName", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.enzymeName = :enzymeName"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByCatalyticActivity", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.catalyticActivity = :catalyticActivity"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByTransferFlag", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.transferFlag = :transferFlag"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByCofactor", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.cofactor = :cofactor")})
public class EnzymePortalEcNumbers implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "EC_INTERNAL_ID")
    private BigDecimal ecInternalId;
    @Column(name = "EC_FAMILY")
    private Integer ecFamily;
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;
    @Column(name = "COFACTOR")
    private String cofactor;
    @JoinColumn(name = "EC_NUMBER", referencedColumnName = "EC_NUMBER")
    @ManyToOne
    private EnzymePortalUniqueEc ecNumber;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
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

    public String getCofactor() {
        return cofactor;
    }

    public void setCofactor(String cofactor) {
        this.cofactor = cofactor;
    }

    public EnzymePortalUniqueEc getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(EnzymePortalUniqueEc ecNumber) {
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
        int hash = 0;
        hash += (ecInternalId != null ? ecInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalEcNumbers)) {
            return false;
        }
        EnzymePortalEcNumbers other = (EnzymePortalEcNumbers) object;
        if ((this.ecInternalId == null && other.ecInternalId != null) || (this.ecInternalId != null && !this.ecInternalId.equals(other.ecInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ecNumber.getEcNumber();
    }

    /**
     *
     * @return the enzyme family representation of the ec class
     */
    //@Override
//    public String getFamily() {
//        Optional<Integer> ec = Optional.ofNullable(this.getEcFamily());
//        if (ec.isPresent()) {
//            return computeEcToFamilyName(ec.get());
//        }
//        return "";
//
//    }
//
//
//    public String computeEcToFamilyName(int ec) {
//
//        if (ec == 1) {
//
//            return EcNumber.EnzymeFamily.OXIDOREDUCTASES.getName();
//        }
//        if (ec == 2) {
//            return EcNumber.EnzymeFamily.TRANSFERASES.getName();
//        }
//        if (ec == 3) {
//            return EcNumber.EnzymeFamily.HYDROLASES.getName();
//        }
//        if (ec == 4) {
//            return EcNumber.EnzymeFamily.LYASES.getName();
//        }
//        if (ec == 5) {
//            return EcNumber.EnzymeFamily.ISOMERASES.getName();
//        }
//        if (ec == 6) {
//            return EcNumber.EnzymeFamily.LIGASES.getName();
//        }
//        if (ec == 7) {
//            return EcNumber.EnzymeFamily.TRANSLOCASES.getName();
//        }
//        return "Invalid Ec Number";
//    }
    
}
