/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_UNIQUE_EC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalUniqueEc.findAll", query = "SELECT e FROM EnzymePortalUniqueEc e")
    , @NamedQuery(name = "EnzymePortalUniqueEc.findByEcNumber", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.ecNumber = :ecNumber")
    , @NamedQuery(name = "EnzymePortalUniqueEc.findByEcFamily", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.ecFamily = :ecFamily")
    , @NamedQuery(name = "EnzymePortalUniqueEc.findByEnzymeName", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.enzymeName = :enzymeName")
    , @NamedQuery(name = "EnzymePortalUniqueEc.findByCatalyticActivity", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.catalyticActivity = :catalyticActivity")
    , @NamedQuery(name = "EnzymePortalUniqueEc.findByTransferFlag", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.transferFlag = :transferFlag")
    , @NamedQuery(name = "EnzymePortalUniqueEc.findByCofactor", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.cofactor = :cofactor")})
public class EnzymePortalUniqueEc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Column(name = "EC_FAMILY")
    private Short ecFamily;
    @Size(max = 300)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;
    @Size(max = 4000)
    @Column(name = "COFACTOR")
    private String cofactor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecNumber")
    private Set<ReactionMechanism> reactionMechanismSet;

    public EnzymePortalUniqueEc() {
    }

    public EnzymePortalUniqueEc(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public Short getEcFamily() {
        return ecFamily;
    }

    public void setEcFamily(Short ecFamily) {
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

    @XmlTransient
    public Set<ReactionMechanism> getReactionMechanismSet() {
        return reactionMechanismSet;
    }

    public void setReactionMechanismSet(Set<ReactionMechanism> reactionMechanismSet) {
        this.reactionMechanismSet = reactionMechanismSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecNumber != null ? ecNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
          if (!(object instanceof EnzymePortalUniqueEc)) {
            return false;
        }
        EnzymePortalUniqueEc other = (EnzymePortalUniqueEc) object;
        return !((this.ecNumber == null && other.ecNumber != null) || (this.ecNumber != null && !this.ecNumber.equals(other.ecNumber)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.model.EnzymePortalUniqueEc[ ecNumber=" + ecNumber + " ]";
    }
    
}
