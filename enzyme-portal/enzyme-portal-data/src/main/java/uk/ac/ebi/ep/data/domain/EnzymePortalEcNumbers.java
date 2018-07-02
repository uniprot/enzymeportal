/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.data.search.model.EcNumber;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalEcNumbers.findAll", query = "SELECT e FROM EnzymePortalEcNumbers e"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcInternalId", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecInternalId = :ecInternalId"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcNumber", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecNumber = :ecNumber"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcFamily", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecFamily = :ecFamily"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEnzymeName", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.enzymeName = :enzymeName"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByCatalyticActivity", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.catalyticActivity = :catalyticActivity")})
public class EnzymePortalEcNumbers extends EcNumber implements Serializable, Comparable<EcNumber> {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EC_INTERNAL_ID")
    private BigDecimal ecInternalId;
    @Size(max = 15)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Column(name = "EC_FAMILY")
    private Integer ecFamily;
    @Size(max = 300)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
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

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
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
        return ecNumber;
    }

    /**
     *
     * @return the enzyme family representation of the ec class
     */
    @Override
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
    @Override
    public Integer getEc() {
        return getEcFamily();

    }

    @Override
    public int compareTo(EcNumber o) {
        return this.ecFamily.compareTo(getEcFamily());

    }


}
