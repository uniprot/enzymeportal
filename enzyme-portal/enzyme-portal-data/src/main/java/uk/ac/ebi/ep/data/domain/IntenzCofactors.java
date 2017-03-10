/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Entity
@Table(name = "INTENZ_COFACTORS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntenzCofactors.findAll", query = "SELECT i FROM IntenzCofactors i"),
    @NamedQuery(name = "IntenzCofactors.findByInternalId", query = "SELECT i FROM IntenzCofactors i WHERE i.internalId = :internalId"),
    @NamedQuery(name = "IntenzCofactors.findByEcNumber", query = "SELECT i FROM IntenzCofactors i WHERE i.ecNumber = :ecNumber"),
    @NamedQuery(name = "IntenzCofactors.findByCofactor", query = "SELECT i FROM IntenzCofactors i WHERE i.cofactor = :cofactor")})
public class IntenzCofactors implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    @Size(max = 15)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Size(max = 4000)
    @Column(name = "COFACTOR")
    private String cofactor;

    public IntenzCofactors() {
    }

    public IntenzCofactors(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public BigDecimal getInternalId() {
        return internalId;
    }

    public void setInternalId(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public String getCofactor() {
        return cofactor;
    }

    public void setCofactor(String cofactor) {
        this.cofactor = cofactor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IntenzCofactors)) {
            return false;
        }
        IntenzCofactors other = (IntenzCofactors) object;
        return !((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.IntenzCofactors[ internalId=" + internalId + " ]";
    }
    
}
