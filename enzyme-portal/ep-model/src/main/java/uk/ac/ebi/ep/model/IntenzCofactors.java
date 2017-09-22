package uk.ac.ebi.ep.model;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @NamedQuery(name = "IntenzCofactors.findByCofactor", query = "SELECT i FROM IntenzCofactors i WHERE i.cofactor = :cofactor")})
public class IntenzCofactors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private Long internalId;
    @Size(max = 4000)
    @Column(name = "COFACTOR")
    private String cofactor;
    @JoinColumn(name = "EC_NUMBER", referencedColumnName = "EC_NUMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private IntenzEnzymes ecNumber;

    public IntenzCofactors() {
    }

    public IntenzCofactors(Long internalId) {
        this.internalId = internalId;
    }

    public Long getInternalId() {
        return internalId;
    }

    public void setInternalId(Long internalId) {
        this.internalId = internalId;
    }

    public String getCofactor() {
        return cofactor;
    }

    public void setCofactor(String cofactor) {
        this.cofactor = cofactor;
    }

    public IntenzEnzymes getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(IntenzEnzymes ecNumber) {
        this.ecNumber = ecNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IntenzCofactors)) {
            return false;
        }
        IntenzCofactors other = (IntenzCofactors) object;
        return !((other.internalId != null && null == this.internalId) || (this.internalId != null && !this.internalId.equals(other.internalId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.IntenzCofactors[ cofactor=" + cofactor + " ]";
    }

}
