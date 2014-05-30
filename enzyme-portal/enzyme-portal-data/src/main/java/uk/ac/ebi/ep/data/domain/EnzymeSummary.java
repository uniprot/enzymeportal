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
@Table(name = "ENZYME_SUMMARY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymeSummary.findAll", query = "SELECT e FROM EnzymeSummary e"),
    @NamedQuery(name = "EnzymeSummary.findByEnzymeId", query = "SELECT e FROM EnzymeSummary e WHERE e.enzymeId = :enzymeId"),
    @NamedQuery(name = "EnzymeSummary.findByDbentryId", query = "SELECT e FROM EnzymeSummary e WHERE e.dbentryId = :dbentryId")
   // @NamedQuery(name = "EnzymeSummary.findByCommentType", query = "SELECT e FROM EnzymeSummary e WHERE e.commentType = :commentType"),
    //@NamedQuery(name = "EnzymeSummary.findByCommentText", query = "SELECT e FROM EnzymeSummary e WHERE e.commentText = :commentText")
})
public class EnzymeSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ENZYME_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_ENZYME_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private Long enzymeId;
    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Column(name = "COMMENT_TYPE")
    private String commentType;
    @Column(name = "COMMENT_TEXT")
    private String commentText;
    @JoinColumn(name = "ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private UniprotEntry accession;

    public EnzymeSummary() {
    }

    public EnzymeSummary(Long enzymeId) {
        this.enzymeId = enzymeId;
    }

    public EnzymeSummary(Long enzymeId, long dbentryId) {
        this.enzymeId = enzymeId;
        this.dbentryId = dbentryId;
    }

    public Long getEnzymeId() {
        return enzymeId;
    }

    public void setEnzymeId(Long enzymeId) {
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

    public UniprotEntry getAccession() {
        return accession;
    }

    public void setAccession(UniprotEntry accession) {
        this.accession = accession;
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
        if (!(object instanceof EnzymeSummary)) {
            return false;
        }
        EnzymeSummary other = (EnzymeSummary) object;
        if ((this.enzymeId == null && other.enzymeId != null) || (this.enzymeId != null && !this.enzymeId.equals(other.enzymeId))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.data.domain.EnzymeSummary[ enzymeId=" + enzymeId + " ]";
//    }
    @Override
    public String toString() {
        return "EnzymeSummary{" + "enzymeId=" + enzymeId + ", dbentryId=" + dbentryId + ", commentType=" + commentType + ", commentText=" + commentText + ", accession=" + accession + '}';
    }
   
    
    
}
