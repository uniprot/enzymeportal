package uk.ac.ebi.ep.data.domain;

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
@Table(name = "INTENZ_ALT_NAMES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntenzAltNames.findAll", query = "SELECT i FROM IntenzAltNames i"),
    @NamedQuery(name = "IntenzAltNames.findByInternalId", query = "SELECT i FROM IntenzAltNames i WHERE i.internalId = :internalId"),
    @NamedQuery(name = "IntenzAltNames.findByAltName", query = "SELECT i FROM IntenzAltNames i WHERE i.altName = :altName")})
public class IntenzAltNames implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private Long internalId;
    @Size(max = 4000)
    @Column(name = "ALT_NAME")
    private String altName;
    @JoinColumn(name = "EC_NUMBER", referencedColumnName = "EC_NUMBER")
    @ManyToOne(fetch = FetchType.LAZY)
    private IntenzEnzymes ecNumber;

    public IntenzAltNames() {
    }

    public IntenzAltNames(Long internalId) {
        this.internalId = internalId;
    }

    public Long getInternalId() {
        return internalId;
    }

    public void setInternalId(Long internalId) {
        this.internalId = internalId;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
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
        if (!(object instanceof IntenzAltNames)) {
            return false;
        }
        IntenzAltNames other = (IntenzAltNames) object;
        return !((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.IntenzAltNames[ AltName=" + altName + " ]";
    }

}
