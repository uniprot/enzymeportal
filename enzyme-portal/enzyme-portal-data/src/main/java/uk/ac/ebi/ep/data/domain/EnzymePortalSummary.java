/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.domain;

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
@Table(name = "ENZYME_PORTAL_SUMMARY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalSummary.findAll", query = "SELECT e FROM EnzymePortalSummary e"),
    @NamedQuery(name = "EnzymePortalSummary.findByEnzymeId", query = "SELECT e FROM EnzymePortalSummary e WHERE e.enzymeId = :enzymeId"),
    @NamedQuery(name = "EnzymePortalSummary.findByDbentryId", query = "SELECT e FROM EnzymePortalSummary e WHERE e.dbentryId = :dbentryId"),
    @NamedQuery(name = "EnzymePortalSummary.findByCommentType", query = "SELECT e FROM EnzymePortalSummary e WHERE e.commentType = :commentType")
    //@NamedQuery(name = "EnzymePortalSummary.findByCommentText", query = "SELECT e FROM EnzymePortalSummary e WHERE e.commentText = :commentText")
})
public class EnzymePortalSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ENZYME_ID")
    private BigDecimal enzymeId;
    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Column(name = "COMMENT_TYPE")
    private String commentType;
    @Column(name = "COMMENT_TEXT")
    private String commentText;

        @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false)
    private UniprotEntry uniprotAccession;
    public EnzymePortalSummary() {
    }

    public EnzymePortalSummary(BigDecimal enzymeId) {
        this.enzymeId = enzymeId;
    }

    public EnzymePortalSummary(BigDecimal enzymeId, long dbentryId) {
        this.enzymeId = enzymeId;
        this.dbentryId = dbentryId;
    }

    public BigDecimal getEnzymeId() {
        return enzymeId;
    }

    public void setEnzymeId(BigDecimal enzymeId) {
        this.enzymeId = enzymeId;
    }

    public long getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(long dbentryId) {
        this.dbentryId = dbentryId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enzymeId != null ? enzymeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalSummary)) {
            return false;
        }
        EnzymePortalSummary other = (EnzymePortalSummary) object;
        if ((this.enzymeId == null && other.enzymeId != null) || (this.enzymeId != null && !this.enzymeId.equals(other.enzymeId))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.data.domain.EnzymePortalSummary[ enzymeId=" + enzymeId + " ]";
//    }
    @Override
    public String toString() {
        return "EnzymePortalSummary{" + "commentType=" + commentType + ", commentText=" + commentText + '}';
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    
    
}
