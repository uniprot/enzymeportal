package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.model.search.model.Summary;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_SUMMARY")
@XmlRootElement

@NamedEntityGraph(name = "summary.graph", attributeNodes = {
    @NamedAttributeNode("uniprotAccession")
})

@SqlResultSetMapping(
        name = "findCommentTextAndAccession",
        classes = {
            @ConstructorResult(
                    targetClass = Summary.class,
                    columns = {
                        @ColumnResult(name = "UNIPROT_ACCESSION"),
                        @ColumnResult(name = "COMMENT_TEXT")

                    }
            )
        }
)

@NamedQueries({
    @NamedQuery(name = "EnzymePortalSummary.findAll", query = "SELECT e FROM EnzymePortalSummary e"),
    @NamedQuery(name = "EnzymePortalSummary.findByEnzymeId", query = "SELECT e FROM EnzymePortalSummary e WHERE e.enzymeId = :enzymeId"),
    @NamedQuery(name = "EnzymePortalSummary.findByDbentryId", query = "SELECT e FROM EnzymePortalSummary e WHERE e.dbentryId = :dbentryId"),
    @NamedQuery(name = "EnzymePortalSummary.findByCommentType", query = "SELECT e FROM EnzymePortalSummary e WHERE e.commentType = :commentType")
    //@NamedQuery(name = "EnzymePortalSummary.findByCommentText", query = "SELECT e FROM EnzymePortalSummary e WHERE e.commentText = :commentText")
})
public class EnzymePortalSummary implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
//    @ManyToOne(optional = false)
//    @BatchSize(size = 10)
//    @Fetch(FetchMode.SELECT)
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

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.uniprotAccession.getProteinName());
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
        final EnzymePortalSummary other = (EnzymePortalSummary) obj;
        if (!Objects.equals(this.uniprotAccession.getProteinName(), other.uniprotAccession.getProteinName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EnzymePortalSummary{" + "enzymeId=" + enzymeId + ", dbentryId=" + dbentryId + ", commentType=" + commentType + ", commentText=" + commentText + '}';
    }

}
