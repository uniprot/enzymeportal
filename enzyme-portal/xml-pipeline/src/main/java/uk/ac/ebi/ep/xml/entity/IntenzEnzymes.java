
package uk.ac.ebi.ep.xml.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "INTENZ_ENZYMES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntenzEnzymes.findAll", query = "SELECT i FROM IntenzEnzymes i"),
    @NamedQuery(name = "IntenzEnzymes.findByInternalId", query = "SELECT i FROM IntenzEnzymes i WHERE i.internalId = :internalId"),
    @NamedQuery(name = "IntenzEnzymes.findByEcNumber", query = "SELECT i FROM IntenzEnzymes i WHERE i.ecNumber = :ecNumber"),
    @NamedQuery(name = "IntenzEnzymes.findByEnzymeName", query = "SELECT i FROM IntenzEnzymes i WHERE i.enzymeName = :enzymeName"),
    @NamedQuery(name = "IntenzEnzymes.findByCatalyticActivity", query = "SELECT i FROM IntenzEnzymes i WHERE i.catalyticActivity = :catalyticActivity"),
    @NamedQuery(name = "IntenzEnzymes.findByTransferFlag", query = "SELECT i FROM IntenzEnzymes i WHERE i.transferFlag = :transferFlag"),
    @NamedQuery(name = "IntenzEnzymes.findByAccPresent", query = "SELECT i FROM IntenzEnzymes i WHERE i.accPresent = :accPresent"),
    @NamedQuery(name = "IntenzEnzymes.findByCofactor", query = "SELECT i FROM IntenzEnzymes i WHERE i.cofactor = :cofactor")})
public class IntenzEnzymes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "INTERNAL_ID")
    private BigInteger internalId;
    @Id
    @Basic(optional = false)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;
    @Column(name = "ACC_PRESENT")
    private Character accPresent;
    @Column(name = "COFACTOR")
    private String cofactor;
    @OneToMany(mappedBy = "ecNumber",fetch = FetchType.EAGER)
    private Set<IntenzAltNames> intenzAltNamesSet;

    public IntenzEnzymes() {
    }

    public IntenzEnzymes(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public BigInteger getInternalId() {
        return internalId;
    }

    public void setInternalId(BigInteger internalId) {
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

    public Character getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(Character transferFlag) {
        this.transferFlag = transferFlag;
    }

    public Character getAccPresent() {
        return accPresent;
    }

    public void setAccPresent(Character accPresent) {
        this.accPresent = accPresent;
    }

    public String getCofactor() {
        return cofactor;
    }

    public void setCofactor(String cofactor) {
        this.cofactor = cofactor;
    }

    @XmlTransient
    public Set<IntenzAltNames> getIntenzAltNamesSet() {
        return intenzAltNamesSet;
    }

    public void setIntenzAltNamesSet(Set<IntenzAltNames> intenzAltNamesSet) {
        this.intenzAltNamesSet = intenzAltNamesSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecNumber != null ? ecNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IntenzEnzymes)) {
            return false;
        }
        IntenzEnzymes other = (IntenzEnzymes) object;
        return !((this.ecNumber == null && other.ecNumber != null) || (this.ecNumber != null && !this.ecNumber.equals(other.ecNumber)));
    }

    @Override
    public String toString() {
        return "IntenzEnzymes{" + "ecNumber=" + ecNumber + ", enzymeName=" + enzymeName + ", catalyticActivity=" + catalyticActivity + ", transferFlag=" + transferFlag + ", accPresent=" + accPresent + ", cofactor=" + cofactor + '}';
    }


    
}
