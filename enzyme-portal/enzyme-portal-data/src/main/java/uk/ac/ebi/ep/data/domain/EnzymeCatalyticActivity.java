/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "ENZYME_CATALYTIC_ACTIVITY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymeCatalyticActivity.findAll", query = "SELECT e FROM EnzymeCatalyticActivity e"),
    @NamedQuery(name = "EnzymeCatalyticActivity.findByActivityInternalId", query = "SELECT e FROM EnzymeCatalyticActivity e WHERE e.activityInternalId = :activityInternalId"),
    @NamedQuery(name = "EnzymeCatalyticActivity.findByCatalyticActivity", query = "SELECT e FROM EnzymeCatalyticActivity e WHERE e.catalyticActivity = :catalyticActivity"),
    @NamedQuery(name = "EnzymeCatalyticActivity.findByDbentryId", query = "SELECT e FROM EnzymeCatalyticActivity e WHERE e.dbentryId = :dbentryId")})
public class EnzymeCatalyticActivity implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVITY_INTERNAL_ID")
    private Long activityInternalId;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "DBENTRY_ID")
    private Long dbentryId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
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

    public Long getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(Long dbentryId) {
        this.dbentryId = dbentryId;
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
        return !((this.activityInternalId == null && other.activityInternalId != null) || (this.activityInternalId != null && !this.activityInternalId.equals(other.activityInternalId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.EnzymeCatalyticActivity[ activityInternalId=" + activityInternalId + " ]";
    }
    
}
