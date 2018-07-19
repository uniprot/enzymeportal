
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
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
 * @author Joseph
 */
@Entity
@Table(name = "REACTION_MECHANISM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReactionMechanism.findAll", query = "SELECT r FROM ReactionMechanism r")
    , @NamedQuery(name = "ReactionMechanism.findByMcsaId", query = "SELECT r FROM ReactionMechanism r WHERE r.mcsaId = :mcsaId")
    , @NamedQuery(name = "ReactionMechanism.findByEnzymeName", query = "SELECT r FROM ReactionMechanism r WHERE r.enzymeName = :enzymeName")
    , @NamedQuery(name = "ReactionMechanism.findByImageId", query = "SELECT r FROM ReactionMechanism r WHERE r.imageId = :imageId")
    , @NamedQuery(name = "ReactionMechanism.findByMechanismDescription", query = "SELECT r FROM ReactionMechanism r WHERE r.mechanismDescription = :mechanismDescription")})
public class ReactionMechanism implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "MCSA_ID")
    private String mcsaId;
    @Size(max = 4000)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "IMAGE_ID")
    private String imageId;
    @Size(max = 4000)
    @Column(name = "MECHANISM_DESCRIPTION")
    private String mechanismDescription;
//    @JoinTable(name = "REACTION_MECHANISM_2_UNIPROT", joinColumns = {
//        @JoinColumn(name = "MCSA_ID", referencedColumnName = "MCSA_ID")}, inverseJoinColumns = {
//        @JoinColumn(name = "ACCESSION", referencedColumnName = "ACCESSION")})
//    @ManyToMany
//    private Set<UniprotEntry> uniprotEntrySet;
//    @JoinColumn(name = "EC_NUMBER", referencedColumnName = "EC_NUMBER")
//    @ManyToOne(optional = false)
//    private EnzymePortalUniqueEc ecNumber;

    public ReactionMechanism() {
    }

    public ReactionMechanism(String mcsaId) {
        this.mcsaId = mcsaId;
    }

    public String getMcsaId() {
        return mcsaId;
    }

    public void setMcsaId(String mcsaId) {
        this.mcsaId = mcsaId;
    }

    public String getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getMechanismDescription() {
        return mechanismDescription;
    }

    public void setMechanismDescription(String mechanismDescription) {
        this.mechanismDescription = mechanismDescription;
    }

//    @XmlTransient
//    public Set<UniprotEntry> getUniprotEntrySet() {
//        return uniprotEntrySet;
//    }
//
//    public void setUniprotEntrySet(Set<UniprotEntry> uniprotEntrySet) {
//        this.uniprotEntrySet = uniprotEntrySet;
//    }
//
//    public EnzymePortalUniqueEc getEcNumber() {
//        return ecNumber;
//    }
//
//    public void setEcNumber(EnzymePortalUniqueEc ecNumber) {
//        this.ecNumber = ecNumber;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mcsaId != null ? mcsaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ReactionMechanism)) {
            return false;
        }
        ReactionMechanism other = (ReactionMechanism) object;
        return !((this.mcsaId == null && other.mcsaId != null) || (this.mcsaId != null && !this.mcsaId.equals(other.mcsaId)));
    }

    @Override
    public String toString() {
        return "ReactionMechanism{" + "mcsaId=" + mcsaId + ", enzymeName=" + enzymeName + ", imageId=" + imageId + ", mechanismDescription=" + mechanismDescription + '}';
    }


    
}
