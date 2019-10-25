package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
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
@Table(name = "ENZYME_PORTAL_DISEASE")
@XmlRootElement

@NamedQuery(name = "EnzymePortalDisease.findAll", query = "SELECT e FROM EnzymePortalDisease e")
@NamedQuery(name = "EnzymePortalDisease.findByDiseaseId", query = "SELECT e FROM EnzymePortalDisease e WHERE e.diseaseId = :diseaseId")
@NamedQuery(name = "EnzymePortalDisease.findByOmimNumber", query = "SELECT e FROM EnzymePortalDisease e WHERE e.omimNumber = :omimNumber")
@NamedQuery(name = "EnzymePortalDisease.findByMeshId", query = "SELECT e FROM EnzymePortalDisease e WHERE e.meshId = :meshId")
@NamedQuery(name = "EnzymePortalDisease.findByEfoId", query = "SELECT e FROM EnzymePortalDisease e WHERE e.efoId = :efoId")
@NamedQuery(name = "EnzymePortalDisease.findByDiseaseName", query = "SELECT e FROM EnzymePortalDisease e WHERE e.diseaseName = :diseaseName")
@NamedQuery(name = "EnzymePortalDisease.findByEvidence", query = "SELECT e FROM EnzymePortalDisease e WHERE e.evidence = :evidence")
@NamedQuery(name = "EnzymePortalDisease.findByDefinition", query = "SELECT e FROM EnzymePortalDisease e WHERE e.definition = :definition")
@NamedQuery(name = "EnzymePortalDisease.findByScore", query = "SELECT e FROM EnzymePortalDisease e WHERE e.score = :score")
@NamedQuery(name = "EnzymePortalDisease.findByUrl", query = "SELECT e FROM EnzymePortalDisease e WHERE e.url = :url")
public class EnzymePortalDisease implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISEASE_ID")
    private Long diseaseId;
    @Size(max = 30)
    @Column(name = "OMIM_NUMBER")
    private String omimNumber;
    @Size(max = 30)
    @Column(name = "MESH_ID")
    private String meshId;
    @Size(max = 30)
    @Column(name = "EFO_ID")
    private String efoId;
    @Size(max = 150)
    @Column(name = "DISEASE_NAME")
    private String diseaseName;
    @Size(max = 4000)
    @Column(name = "EVIDENCE")
    private String evidence;
    @Size(max = 4000)
    @Column(name = "DEFINITION")
    private String definition;
    @Size(max = 150)
    @Column(name = "SCORE")
    private String score;
    @Size(max = 255)
    @Column(name = "URL")
    private String url;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false)
    private UniprotEntry uniprotAccession;

}
