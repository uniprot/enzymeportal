
package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_FAMILY_NAME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalFamilyName.findAll", query = "SELECT e FROM EnzymePortalFamilyName e"),
    @NamedQuery(name = "EnzymePortalFamilyName.findByFamilyName", query = "SELECT e FROM EnzymePortalFamilyName e WHERE e.familyName = :familyName"),
    @NamedQuery(name = "EnzymePortalFamilyName.findByFamilyNameId", query = "SELECT e FROM EnzymePortalFamilyName e WHERE e.familyNameId = :familyNameId"),
    @NamedQuery(name = "EnzymePortalFamilyName.findByFamilyGroupId", query = "SELECT e FROM EnzymePortalFamilyName e WHERE e.familyGroupId = :familyGroupId")})
public class EnzymePortalFamilyName implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 4000)
    @Column(name = "FAMILY_NAME")
    private String familyName;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FAMILY_NAME_ID")
    private BigDecimal familyNameId;
    @Size(max = 10)
    @Column(name = "FAMILY_GROUP_ID")
    private String familyGroupId;

    public EnzymePortalFamilyName() {
    }

    public EnzymePortalFamilyName(BigDecimal familyNameId) {
        this.familyNameId = familyNameId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public BigDecimal getFamilyNameId() {
        return familyNameId;
    }

    public void setFamilyNameId(BigDecimal familyNameId) {
        this.familyNameId = familyNameId;
    }

    public String getFamilyGroupId() {
        return familyGroupId;
    }

    public void setFamilyGroupId(String familyGroupId) {
        this.familyGroupId = familyGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (familyNameId != null ? familyNameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalFamilyName)) {
            return false;
        }
        EnzymePortalFamilyName other = (EnzymePortalFamilyName) object;
        return !((this.familyNameId == null && other.familyNameId != null) || (this.familyNameId != null && !this.familyNameId.equals(other.familyNameId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.dataservice.entities.EnzymePortalFamilyName[ familyNameId=" + familyNameId + " ]";
    }
    
}
