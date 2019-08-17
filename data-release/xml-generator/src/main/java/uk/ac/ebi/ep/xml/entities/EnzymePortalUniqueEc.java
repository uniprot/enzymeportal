package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "ENZYME_PORTAL_UNIQUE_EC")
@XmlRootElement
@NamedQuery(name = "EnzymePortalUniqueEc.findAll", query = "SELECT e FROM EnzymePortalUniqueEc e")
@NamedQuery(name = "EnzymePortalUniqueEc.findByEcNumber", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.ecNumber = :ecNumber")
@NamedQuery(name = "EnzymePortalUniqueEc.findByEcFamily", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.ecFamily = :ecFamily")
@NamedQuery(name = "EnzymePortalUniqueEc.findByEnzymeName", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.enzymeName = :enzymeName")
@NamedQuery(name = "EnzymePortalUniqueEc.findByCatalyticActivity", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.catalyticActivity = :catalyticActivity")
@NamedQuery(name = "EnzymePortalUniqueEc.findByTransferFlag", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.transferFlag = :transferFlag")
@NamedQuery(name = "EnzymePortalUniqueEc.findByCofactor", query = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.cofactor = :cofactor")
public class EnzymePortalUniqueEc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Column(name = "EC_FAMILY")
    private Short ecFamily;
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;
    @Column(name = "COFACTOR")
    private String cofactor;
    @OneToMany(mappedBy = "ecNumber", fetch = FetchType.EAGER)
    private Set<IntenzAltNames> intenzAltNamesSet;

}
