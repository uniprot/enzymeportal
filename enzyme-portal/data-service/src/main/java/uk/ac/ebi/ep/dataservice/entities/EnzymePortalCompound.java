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
@Table(name = "ENZYME_PORTAL_COMPOUND")
@XmlRootElement

@NamedQuery(name = "EnzymePortalCompound.findAll", query = "SELECT e FROM EnzymePortalCompound e")
@NamedQuery(name = "EnzymePortalCompound.findByCompoundInternalId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundInternalId = :compoundInternalId")
@NamedQuery(name = "EnzymePortalCompound.findByCompoundId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundId = :compoundId")
@NamedQuery(name = "EnzymePortalCompound.findByCompoundName", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundName = :compoundName")
@NamedQuery(name = "EnzymePortalCompound.findByCompoundSource", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundSource = :compoundSource")
@NamedQuery(name = "EnzymePortalCompound.findByRelationship", query = "SELECT e FROM EnzymePortalCompound e WHERE e.relationship = :relationship")
@NamedQuery(name = "EnzymePortalCompound.findByUrl", query = "SELECT e FROM EnzymePortalCompound e WHERE e.url = :url")
@NamedQuery(name = "EnzymePortalCompound.findByCompoundRole", query = "SELECT e FROM EnzymePortalCompound e WHERE e.compoundRole = :compoundRole")
@NamedQuery(name = "EnzymePortalCompound.findByNote", query = "SELECT e FROM EnzymePortalCompound e WHERE e.note = :note")
@NamedQuery(name = "EnzymePortalCompound.findByChemblTargetId", query = "SELECT e FROM EnzymePortalCompound e WHERE e.chemblTargetId = :chemblTargetId")
public class EnzymePortalCompound implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPOUND_INTERNAL_ID")
    private BigDecimal compoundInternalId;
    @Size(max = 30)
    @Column(name = "COMPOUND_ID")
    private String compoundId;
    @Size(max = 4000)
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Size(max = 30)
    @Column(name = "COMPOUND_SOURCE")
    private String compoundSource;
    @Size(max = 30)
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Size(max = 255)
    @Column(name = "URL")
    private String url;
    @Size(max = 30)
    @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
    @Size(max = 4000)
    @Column(name = "NOTE")
    private String note;
    @Size(max = 20)
    @Column(name = "CHEMBL_TARGET_ID")
    private String chemblTargetId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

}
