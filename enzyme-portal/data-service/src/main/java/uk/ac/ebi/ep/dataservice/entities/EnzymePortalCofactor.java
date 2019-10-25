
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
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ENZYME_PORTAL_COFACTOR")
@XmlRootElement
    @NamedQuery(name = "EnzymePortalCofactor.findAll", query = "SELECT e FROM EnzymePortalCofactor e")
    @NamedQuery(name = "EnzymePortalCofactor.findByCofactorInternalId", query = "SELECT e FROM EnzymePortalCofactor e WHERE e.cofactorInternalId = :cofactorInternalId")
    @NamedQuery(name = "EnzymePortalCofactor.findByCofactorId", query = "SELECT e FROM EnzymePortalCofactor e WHERE e.cofactorId = :cofactorId")
    @NamedQuery(name = "EnzymePortalCofactor.findByCofactorName", query = "SELECT e FROM EnzymePortalCofactor e WHERE e.cofactorName = :cofactorName")
public class EnzymePortalCofactor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COFACTOR_INTERNAL_ID")
    private BigDecimal cofactorInternalId;
    @Size(max = 20)
    @Column(name = "COFACTOR_ID")
    private String cofactorId;
    @Size(max = 4000)
    @Column(name = "COFACTOR_NAME")
    private String cofactorName;


}
