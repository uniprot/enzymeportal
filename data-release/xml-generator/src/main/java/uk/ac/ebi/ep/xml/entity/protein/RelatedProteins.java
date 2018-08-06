package uk.ac.ebi.ep.xml.entity.protein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "RELATED_PROTEINS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelatedProteins.findAll", query = "SELECT r FROM RelatedProteins r"),
    @NamedQuery(name = "RelatedProteins.findByRelProtInternalId", query = "SELECT r FROM RelatedProteins r WHERE r.relProtInternalId = :relProtInternalId")
    //@NamedQuery(name = "RelatedProteins.findByNamePrefix", query = "SELECT r FROM RelatedProteins r WHERE r.namePrefix = :namePrefix")
})
public class RelatedProteins implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @Basic(optional = false)
    @Column(name = "REL_PROT_INTERNAL_ID")
    private long relProtInternalId;
    @Column(name = "NAME_PREFIX")
    private String namePrefix;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "relatedProteinsId")

    private List<UniprotEntry> uniprotEntrySet;

    public RelatedProteins() {
    }

    public RelatedProteins(long relProtInternalId) {
        this.relProtInternalId = relProtInternalId;
    }

    public long getRelProtInternalId() {
        return relProtInternalId;
    }

    public void setRelProtInternalId(long relProtInternalId) {
        this.relProtInternalId = relProtInternalId;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @XmlTransient
    public List<UniprotEntry> getUniprotEntrySet() {


        if (uniprotEntrySet == null) {
            uniprotEntrySet = new ArrayList<>();
        }

        return uniprotEntrySet;
    }

    public void setUniprotEntrySet(List<UniprotEntry> uniprotEntrySet) {
        this.uniprotEntrySet = uniprotEntrySet;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.RelatedProteins[" + relProtInternalId + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.relProtInternalId ^ (this.relProtInternalId >>> 32));
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
        final RelatedProteins other = (RelatedProteins) obj;
        return this.relProtInternalId == other.relProtInternalId;
    }

}
