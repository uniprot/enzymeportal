/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model;

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
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_SP_EXP_EVIDENCE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymeSpExpEvidence.findAll", query = "SELECT e FROM EnzymeSpExpEvidence e"),
    @NamedQuery(name = "EnzymeSpExpEvidence.findByAccession", query = "SELECT e FROM EnzymeSpExpEvidence e WHERE e.accession = :accession"),
    @NamedQuery(name = "EnzymeSpExpEvidence.findByEvidenceLine", query = "SELECT e FROM EnzymeSpExpEvidence e WHERE e.evidenceLine = :evidenceLine"),
    @NamedQuery(name = "EnzymeSpExpEvidence.findByEvidenceInternalId", query = "SELECT e FROM EnzymeSpExpEvidence e WHERE e.evidenceInternalId = :evidenceInternalId")})
public class EnzymeSpExpEvidence implements Serializable {
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
    @SequenceGenerator(allocationSize = 1, name = "spSeqGenerator", sequenceName = "SEQ_SP_EVIDENCE_ID")
    @GeneratedValue(generator = "spSeqGenerator", strategy = GenerationType.SEQUENCE)
    private BigDecimal evidenceInternalId;

    public EnzymeSpExpEvidence() {
    }

    public EnzymeSpExpEvidence(BigDecimal evidenceInternalId) {
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
        final EnzymeSpExpEvidence other = (EnzymeSpExpEvidence) obj;
        if (!Objects.equals(this.accession, other.accession)) {
            return false;
        }
        return Objects.equals(this.evidenceLine, other.evidenceLine);
    }

    @Override
    public String toString() {
        return "EnzymeSpExpEvidence{" + "accession=" + accession + ", evidenceLine=" + evidenceLine + '}';
    }
    
    
    


}
