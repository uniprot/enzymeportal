package uk.ac.ebi.ep.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_CATALYTIC_ACTIVITY")
@XmlRootElement

@NamedEntityGraph(name = "ActivityEntityGraph", attributeNodes = {  
    @NamedAttributeNode("uniprotAccession")
})

@NamedQueries({
    @NamedQuery(name = "EnzymeCatalyticActivity.findAll", query = "SELECT e FROM EnzymeCatalyticActivity e"),
    @NamedQuery(name = "EnzymeCatalyticActivity.findByActivityInternalId", query = "SELECT e FROM EnzymeCatalyticActivity e WHERE e.activityInternalId = :activityInternalId"),
    @NamedQuery(name = "EnzymeCatalyticActivity.findByCatalyticActivity", query = "SELECT e FROM EnzymeCatalyticActivity e WHERE e.catalyticActivity = :catalyticActivity")})
public class EnzymeCatalyticActivity implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ACTIVITY_INTERNAL_ID")
    private Long activityInternalId;
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    public EnzymeCatalyticActivity() {
    }

    public EnzymeCatalyticActivity(Long activityInternalId) {
        this.activityInternalId = activityInternalId;
    }

    public Long getActivityInternalId() {
        return activityInternalId;
    }

    public void setActivityInternalId(Long activityInternalId) {
        this.activityInternalId = activityInternalId;
    }

    public String getCatalyticActivity() {
        return catalyticActivity;
    }

    public void setCatalyticActivity(String catalyticActivity) {
        this.catalyticActivity = catalyticActivity;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityInternalId != null ? activityInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymeCatalyticActivity)) {
            return false;
        }
        EnzymeCatalyticActivity other = (EnzymeCatalyticActivity) object;
        if ((this.activityInternalId == null && other.activityInternalId != null) || (this.activityInternalId != null && !this.activityInternalId.equals(other.activityInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return catalyticActivity;
    }


    
}
