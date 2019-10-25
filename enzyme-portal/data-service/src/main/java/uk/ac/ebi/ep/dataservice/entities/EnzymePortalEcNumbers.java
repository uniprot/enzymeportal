package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement

@NamedQuery(name = "EnzymePortalEcNumbers.findAll", query = "SELECT e FROM EnzymePortalEcNumbers e")
@NamedQuery(name = "EnzymePortalEcNumbers.findByEcInternalId", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecInternalId = :ecInternalId")
@NamedQuery(name = "EnzymePortalEcNumbers.findByEcFamily", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecFamily = :ecFamily")
@NamedQuery(name = "EnzymePortalEcNumbers.findByEnzymeName", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.enzymeName = :enzymeName")
@NamedQuery(name = "EnzymePortalEcNumbers.findByCatalyticActivity", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.catalyticActivity = :catalyticActivity")
@NamedQuery(name = "EnzymePortalEcNumbers.findByTransferFlag", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.transferFlag = :transferFlag")
@NamedQuery(name = "EnzymePortalEcNumbers.findByCofactor", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.cofactor = :cofactor")
public class EnzymePortalEcNumbers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EC_INTERNAL_ID")
    private BigDecimal ecInternalId;
    @Column(name = "EC_FAMILY")
    private Short ecFamily;
    @Size(max = 300)
    @Column(name = "ENZYME_NAME")
    private String enzymeName;
    @Size(max = 4000)
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    private Character transferFlag;
    @Size(max = 4000)
    @Column(name = "COFACTOR")
    private String cofactor;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

}
