package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
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
    @ToString.Include
    private String familyName;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FAMILY_NAME_ID")
    @EqualsAndHashCode.Include
    @ToString.Include
    private BigDecimal familyNameId;
    @Size(max = 10)
    @Column(name = "FAMILY_GROUP_ID")
    private String familyGroupId;

}
