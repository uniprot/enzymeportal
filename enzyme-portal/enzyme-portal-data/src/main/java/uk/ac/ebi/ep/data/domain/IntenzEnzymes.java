package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Entity
//@EqualsAndHashCode
@Getter
@Setter
@ToString
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INTENZ_ENZYMES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntenzEnzymes.findAll", query = "SELECT i FROM IntenzEnzymes i"),
    @NamedQuery(name = "IntenzEnzymes.findByInternalId", query = "SELECT i FROM IntenzEnzymes i WHERE i.internalId = :internalId"),
    @NamedQuery(name = "IntenzEnzymes.findByEcNumber", query = "SELECT i FROM IntenzEnzymes i WHERE i.ecNumber = :ecNumber"),
    @NamedQuery(name = "IntenzEnzymes.findByEnzymeName", query = "SELECT i FROM IntenzEnzymes i WHERE i.enzymeName = :enzymeName"),
    @NamedQuery(name = "IntenzEnzymes.findByCatalyticActivity", query = "SELECT i FROM IntenzEnzymes i WHERE i.catalyticActivity = :catalyticActivity")})
public class IntenzEnzymes implements Comparable<IntenzEnzymes>, Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private Long internalId;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;
    @OneToMany(mappedBy = "ecNumber", fetch = FetchType.EAGER)
    //@Fetch(FetchMode.JOIN)
    private Set<IntenzCofactors> intenzCofactorsSet;
    @OneToMany(mappedBy = "ecNumber", fetch = FetchType.EAGER)
    //@Fetch(FetchMode.JOIN)
    private Set<IntenzAltNames> intenzAltNamesSet;
    private static final long serialVersionUID = 1L;
    @Size(max = 15)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Size(max = 300)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;

    public IntenzEnzymes(Long internalId) {
        this.internalId = internalId;
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
    public int compareTo(IntenzEnzymes o) {
        return this.getEcNumber().compareTo(o.getEcNumber());
    }

    public Long getInternalId() {
        return internalId;
    }

    public void setInternalId(Long internalId) {
        this.internalId = internalId;
    }

    public Character getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(Character transferFlag) {
        this.transferFlag = transferFlag;
    }

    @XmlTransient
    public Set<IntenzCofactors> getIntenzCofactorsSet() {
        if (intenzCofactorsSet == null) {
            intenzCofactorsSet = new HashSet<>();
        }
        return intenzCofactorsSet;
    }

    public void setIntenzCofactorsSet(Set<IntenzCofactors> intenzCofactorsSet) {
        this.intenzCofactorsSet = intenzCofactorsSet;
    }

    @XmlTransient
    public Set<IntenzAltNames> getIntenzAltNamesSet() {
        if (intenzAltNamesSet == null) {
            intenzAltNamesSet = new HashSet<>();
        }
        return intenzAltNamesSet;
    }

    public void setIntenzAltNamesSet(Set<IntenzAltNames> intenzAltNamesSet) {
        this.intenzAltNamesSet = intenzAltNamesSet;
    }

}
