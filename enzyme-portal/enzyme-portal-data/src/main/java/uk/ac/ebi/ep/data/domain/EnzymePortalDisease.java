/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.view.DiseaseViews;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "ENZYME_PORTAL_DISEASE")
@XmlRootElement
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

@SqlResultSetMappings({
    @SqlResultSetMapping(
            name = "browseDiseases",
            classes = {
                @ConstructorResult(
                        targetClass = DiseaseViews.class,
                        columns = {
                            @ColumnResult(name = "OMIM_NUMBER"),
                            @ColumnResult(name = "DISEASE_NAME")

                        }
                )
            }
    )

})

public class EnzymePortalDisease extends Disease implements Serializable, Comparable<EnzymePortalDisease> {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

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

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        hash = 89 * hash + Objects.hashCode(this.omimNumber);
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
        if (!Objects.equals(this.omimNumber, other.omimNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.EnzymePortalDisease[ diseaseId=" + diseaseId + " ]";
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
        return omimNumber;
    }

    @Override
    public String getName() {

        return diseaseName.replaceAll(",", "").split("\\(")[0];
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
