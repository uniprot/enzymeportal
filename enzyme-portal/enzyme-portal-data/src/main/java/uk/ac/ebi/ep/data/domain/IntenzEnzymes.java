/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
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
@Table(name = "INTENZ_ENZYMES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntenzEnzymes.findAll", query = "SELECT i FROM IntenzEnzymes i"),
    @NamedQuery(name = "IntenzEnzymes.findByInternalId", query = "SELECT i FROM IntenzEnzymes i WHERE i.internalId = :internalId"),
    @NamedQuery(name = "IntenzEnzymes.findByEcNumber", query = "SELECT i FROM IntenzEnzymes i WHERE i.ecNumber = :ecNumber"),
    @NamedQuery(name = "IntenzEnzymes.findByEnzymeName", query = "SELECT i FROM IntenzEnzymes i WHERE i.enzymeName = :enzymeName"),
    @NamedQuery(name = "IntenzEnzymes.findByCatalyticActivity", query = "SELECT i FROM IntenzEnzymes i WHERE i.catalyticActivity = :catalyticActivity")})
public class IntenzEnzymes implements Comparable<IntenzEnzymes>, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    @Size(max = 15)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Size(max = 300)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;

    public IntenzEnzymes() {
    }

    public IntenzEnzymes(BigDecimal internalId) {
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.ecNumber);
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
        final IntenzEnzymes other = (IntenzEnzymes) obj;
        return Objects.equals(this.ecNumber, other.ecNumber);
    }

    @Override
    public String toString() {
        return "IntenzEnzymes{" + "ecNumber=" + ecNumber + ", enzymeName=" + enzymeName + '}';
    }

    @Override
    public int compareTo(IntenzEnzymes o) {
      return this.getEcNumber().compareTo(o.getEcNumber());
    }
    
}
