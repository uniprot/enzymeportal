package uk.ac.ebi.ep.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_METABOLITE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalMetabolite.findAll", query = "SELECT e FROM EnzymePortalMetabolite e"),
    @NamedQuery(name = "EnzymePortalMetabolite.findByMetaboliteInternalId", query = "SELECT e FROM EnzymePortalMetabolite e WHERE e.metaboliteInternalId = :metaboliteInternalId"),
    @NamedQuery(name = "EnzymePortalMetabolite.findByMetaboliteId", query = "SELECT e FROM EnzymePortalMetabolite e WHERE e.metaboliteId = :metaboliteId"),
    @NamedQuery(name = "EnzymePortalMetabolite.findByMetaboliteName", query = "SELECT e FROM EnzymePortalMetabolite e WHERE e.metaboliteName = :metaboliteName"),
    @NamedQuery(name = "EnzymePortalMetabolite.findByMetaboliteUrl", query = "SELECT e FROM EnzymePortalMetabolite e WHERE e.metaboliteUrl = :metaboliteUrl")})
public class EnzymePortalMetabolite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "METABOLITE_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "metaboliteSeqGenerator", sequenceName = "SEQ_EZP_METABOLITE")
    @GeneratedValue(generator = "metaboliteSeqGenerator", strategy = GenerationType.SEQUENCE)
    private Long metaboliteInternalId;
    @Size(max = 50)
    @Column(name = "METABOLITE_ID")
    private String metaboliteId;
    @Size(max = 50)
    @Column(name = "METABOLITE_NAME")
    private String metaboliteName;
    @Size(max = 250)
    @Column(name = "METABOLITE_URL")
    private String metaboliteUrl;

    public EnzymePortalMetabolite() {
    }

    public EnzymePortalMetabolite(Long metaboliteInternalId) {
        this.metaboliteInternalId = metaboliteInternalId;
    }

    public Long getMetaboliteInternalId() {
        return metaboliteInternalId;
    }

    public void setMetaboliteInternalId(Long metaboliteInternalId) {
        this.metaboliteInternalId = metaboliteInternalId;
    }

    public String getMetaboliteId() {
        return metaboliteId;
    }

    public void setMetaboliteId(String metaboliteId) {
        this.metaboliteId = metaboliteId;
    }

    public String getMetaboliteName() {
        return metaboliteName;
    }

    public void setMetaboliteName(String metaboliteName) {
        this.metaboliteName = metaboliteName;
    }

    public String getMetaboliteUrl() {
        return metaboliteUrl;
    }

    public void setMetaboliteUrl(String metaboliteUrl) {
        this.metaboliteUrl = metaboliteUrl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metaboliteInternalId != null ? metaboliteInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EnzymePortalMetabolite)) {
            return false;
        }
        EnzymePortalMetabolite other = (EnzymePortalMetabolite) object;
        return !((this.metaboliteInternalId == null && other.metaboliteInternalId != null) || (this.metaboliteInternalId != null && !this.metaboliteInternalId.equals(other.metaboliteInternalId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.model.EnzymePortalMetabolite[ metaboliteInternalId=" + metaboliteInternalId + " ]";
    }

}
