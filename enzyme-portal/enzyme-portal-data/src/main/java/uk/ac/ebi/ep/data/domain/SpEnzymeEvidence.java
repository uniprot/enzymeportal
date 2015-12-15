/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Entity
@Table(name = "SP_ENZYME_EVIDENCE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SpEnzymeEvidence.findAll", query = "SELECT s FROM SpEnzymeEvidence s"),
    @NamedQuery(name = "SpEnzymeEvidence.findByAccession", query = "SELECT s FROM SpEnzymeEvidence s WHERE s.accession = :accession"),
    @NamedQuery(name = "SpEnzymeEvidence.findByEvidenceLine", query = "SELECT s FROM SpEnzymeEvidence s WHERE s.evidenceLine = :evidenceLine"),
    @NamedQuery(name = "SpEnzymeEvidence.findByEvidenceInternalId", query = "SELECT s FROM SpEnzymeEvidence s WHERE s.evidenceInternalId = :evidenceInternalId")})
public class SpEnzymeEvidence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 15)
    @Column(name = "ACCESSION")
    private String accession;
    @Size(max = 30)
    @Column(name = "EVIDENCE_LINE")
    private String evidenceLine;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVIDENCE_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_SP_EVIDENCE_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.SEQUENCE)
    private BigDecimal evidenceInternalId;

    public SpEnzymeEvidence() {
    }

    public SpEnzymeEvidence(BigDecimal evidenceInternalId) {
        this.evidenceInternalId = evidenceInternalId;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getEvidenceLine() {
        return evidenceLine;
    }

    public void setEvidenceLine(String evidenceLine) {
        this.evidenceLine = evidenceLine;
    }

    public BigDecimal getEvidenceInternalId() {
        return evidenceInternalId;
    }

    public void setEvidenceInternalId(BigDecimal evidenceInternalId) {
        this.evidenceInternalId = evidenceInternalId;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (evidenceInternalId != null ? evidenceInternalId.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof SpEnzymeEvidence)) {
//            return false;
//        }
//        SpEnzymeEvidence other = (SpEnzymeEvidence) object;
//        return !((this.evidenceInternalId == null && other.evidenceInternalId != null) || (this.evidenceInternalId != null && !this.evidenceInternalId.equals(other.evidenceInternalId)));
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.accession);
        hash = 59 * hash + Objects.hashCode(this.evidenceLine);
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
        final SpEnzymeEvidence other = (SpEnzymeEvidence) obj;
        if (!Objects.equals(this.accession, other.accession)) {
            return false;
        }
        if (!Objects.equals(this.evidenceLine, other.evidenceLine)) {
            return false;
        }
        return true;
    }
    
    
    

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.SpEnzymeEvidence[ evidenceInternalId=" + evidenceInternalId + " ]";
    }

}
