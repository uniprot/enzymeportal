/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author joseph
 */
@Entity
@Table( name = "DISEASE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disease.findAll", query = "SELECT d FROM Disease d"),
    @NamedQuery(name = "Disease.findByAccession", query = "SELECT d FROM Disease d WHERE d.accession = :accession"),
    //@NamedQuery(name = "Disease.findByDiseaseId", query = "SELECT d FROM Disease d WHERE d.diseaseId = :diseaseId"),
    @NamedQuery(name = "Disease.findByOmimNumber", query = "SELECT d FROM Disease d WHERE d.omimNumber = :omimNumber"),
    @NamedQuery(name = "Disease.findByMeshId", query = "SELECT d FROM Disease d WHERE d.meshId = :meshId"),
    @NamedQuery(name = "Disease.findByEfoId", query = "SELECT d FROM Disease d WHERE d.efoId = :efoId"),
    @NamedQuery(name = "Disease.findByDiseaseName", query = "SELECT d FROM Disease d WHERE d.diseaseName = :diseaseName"),
    @NamedQuery(name = "Disease.findByScore", query = "SELECT d FROM Disease d WHERE d.score = :score")})
public class Disease implements Serializable {
    @Column(name = "EVIDENCE")
    private String evidence;
    @Column(name = "DEFINITION")
    private String definition;
    @JoinColumn(name = "ACCESSION",insertable = false, updatable = false, referencedColumnName = "ACCESSION" )
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private UniprotEntry accession;
    private static final long serialVersionUID = 1L;
    @Column(name = "ACCESSION")
    private String uniprotaccession;
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
    @Column(name = "SCORE")
    private String score;

    public Disease() {
    }

    public Disease(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getUniprotaccession() {
        return uniprotaccession;
    }

    public void setUniprotaccession(String uniprotaccession) {
        this.uniprotaccession = uniprotaccession;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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
        if (!(object instanceof Disease)) {
            return false;
        }
        Disease other = (Disease) object;
        if ((this.diseaseId == null && other.diseaseId != null) || (this.diseaseId != null && !this.diseaseId.equals(other.diseaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Disease{" + "accession=" + accession + ", diseaseName=" + diseaseName + '}';
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

    public UniprotEntry getAccession() {
        return accession;
    }

    public void setAccession(UniprotEntry accession) {
        this.accession = accession;
    }



   
}
