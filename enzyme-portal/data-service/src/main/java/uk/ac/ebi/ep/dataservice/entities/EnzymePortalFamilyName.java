package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ENZYME_PORTAL_FAMILY_NAME")
@XmlRootElement
@NamedQuery(name = "EnzymePortalFamilyName.findAll", query = "SELECT e FROM EnzymePortalFamilyName e")
@NamedQuery(name = "EnzymePortalFamilyName.findByFamilyName", query = "SELECT e FROM EnzymePortalFamilyName e WHERE e.familyName = :familyName")
@NamedQuery(name = "EnzymePortalFamilyName.findByFamilyNameId", query = "SELECT e FROM EnzymePortalFamilyName e WHERE e.familyNameId = :familyNameId")
@NamedQuery(name = "EnzymePortalFamilyName.findByFamilyGroupId", query = "SELECT e FROM EnzymePortalFamilyName e WHERE e.familyGroupId = :familyGroupId")
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.familyNameId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymePortalFamilyName other = (EnzymePortalFamilyName) obj;
        return Objects.equals(this.familyNameId, other.familyNameId);
    }
    
    
    @Override
    public String toString() {
        return "uk.ac.ebi.ep.dataservice.entities.EnzymePortalFamilyName[ familyNameId=" + familyNameId + " ]";
    }

}
