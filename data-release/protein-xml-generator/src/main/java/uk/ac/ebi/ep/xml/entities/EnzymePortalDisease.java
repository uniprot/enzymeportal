/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
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
public class EnzymePortalDisease implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DISEASE_ID")
    private BigDecimal diseaseId;
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
    @ManyToOne(optional = false)
    private UniprotEntry uniprotAccession;

    public EnzymePortalDisease() {
    }

    public EnzymePortalDisease(BigDecimal diseaseId) {
        this.diseaseId = diseaseId;
    }

    public BigDecimal getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(BigDecimal diseaseId) {
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

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diseaseId != null ? diseaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalDisease)) {
            return false;
        }
        EnzymePortalDisease other = (EnzymePortalDisease) object;
        if ((this.diseaseId == null && other.diseaseId != null) || (this.diseaseId != null && !this.diseaseId.equals(other.diseaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.xml.entities.EnzymePortalDisease[ diseaseId=" + diseaseId + " ]";
    }
    
}
