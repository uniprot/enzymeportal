/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.data.search.model.Disease;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_DISEASE")
@XmlRootElement

@NamedEntityGraph(name = "DiseaseEntityGraph", attributeNodes = {
    @NamedAttributeNode(value = "uniprotAccession", subgraph = "relatedProteinsId"),},
        subgraphs = {
            @NamedSubgraph(
                    name = "uniprotAccession",
                    attributeNodes = {
                        @NamedAttributeNode("relatedProteinsId")}
            )
        }
)

@NamedQueries({
    @NamedQuery(name = "EnzymePortalDisease.findAll", query = "SELECT e FROM EnzymePortalDisease e"),
    @NamedQuery(name = "EnzymePortalDisease.findByDiseaseId", query = "SELECT e FROM EnzymePortalDisease e WHERE e.diseaseId = :diseaseId"),
    @NamedQuery(name = "EnzymePortalDisease.findByOmimNumber", query = "SELECT e FROM EnzymePortalDisease e WHERE e.omimNumber = :omimNumber"),
    @NamedQuery(name = "EnzymePortalDisease.findByMeshId", query = "SELECT e FROM EnzymePortalDisease e WHERE e.meshId = :meshId"),
    @NamedQuery(name = "EnzymePortalDisease.findByEfoId", query = "SELECT e FROM EnzymePortalDisease e WHERE e.efoId = :efoId"),
    @NamedQuery(name = "EnzymePortalDisease.findByDiseaseName", query = "SELECT e FROM EnzymePortalDisease e WHERE e.diseaseName = :diseaseName"),
    @NamedQuery(name = "EnzymePortalDisease.findByEvidence", query = "SELECT e FROM EnzymePortalDisease e WHERE e.evidence = :evidence"),
    @NamedQuery(name = "EnzymePortalDisease.findByDefinition", query = "SELECT e FROM EnzymePortalDisease e WHERE e.definition = :definition"),
    @NamedQuery(name = "EnzymePortalDisease.findByScore", query = "SELECT e FROM EnzymePortalDisease e WHERE e.score = :score"),
    @NamedQuery(name = "EnzymePortalDisease.findByUrl", query = "SELECT e FROM EnzymePortalDisease e WHERE e.url = :url")})
public class EnzymePortalDisease extends Disease implements Serializable, Comparable<EnzymePortalDisease> {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    @Id
    @Basic(optional = false)
    @Column(name = "DISEASE_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_DISEASE_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private Long diseaseId;
    @Column(name = "OMIM_NUMBER")
    private String omimNumber;
    @Column(name = "MESH_ID")
    private String meshId;
    @Column(name = "EFO_ID")
    private String efoId;
    @Column(name = "DISEASE_NAME")
    private String diseaseName;
    @Column(name = "EVIDENCE")
    private String evidence;
    @Column(name = "DEFINITION")
    private String definition;
    @Column(name = "SCORE")
    private String score;
    @Column(name = "URL")
    private String url;

    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    private static final Comparator<String> NAME_COMPARATOR
            = new ChemicalNameComparator();

    public EnzymePortalDisease() {
    }

    public EnzymePortalDisease(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getOmimNumber() {
        return omimNumber;
    }

    public void setOmimNumber(String omimNumber) {
        this.omimNumber = omimNumber;
    }

    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    public String getEfoId() {
        return efoId;
    }

    public void setEfoId(String efoId) {
        this.efoId = efoId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "EnzymePortalDisease{" + "diseaseId=" + diseaseId + ", diseaseName=" + diseaseName + ", evidence=" + evidence + ", definition=" + definition + ", url=" + url + '}';
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.diseaseName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymePortalDisease other = (EnzymePortalDisease) obj;
        if (!Objects.equals(this.diseaseName, other.diseaseName)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(EnzymePortalDisease o) {
        return NAME_COMPARATOR.compare(this.getDiseaseName(),
                o.getDiseaseName());
    }

    @Override
    public List<String> getEvidences() {
        if (evidences == null) {
            evidences = new ArrayList<>();
        }

        evidences.add(getEvidence());
        return evidences;
    }

    @Override
    public void setEvidences(List<String> evidences) {
        this.evidences = evidences;
    }

    @Override
    public String getId() {
        return meshId;
    }

    @Override
    public String getName() {
        return diseaseName;
    }

    @Override
    public String getDescription() {
        return definition;
    }

    /**
     * Gets the value of the selected property.
     *
     * @return selected
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * Gets the value of the numEnzyme property.
     *
     * @return numEnzyme
     */
    @Override
    public int getNumEnzyme() {
        return numEnzyme;
    }

}
