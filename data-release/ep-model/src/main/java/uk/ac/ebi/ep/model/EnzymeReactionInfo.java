package uk.ac.ebi.ep.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_REACTION_INFO")
public class EnzymeReactionInfo implements Serializable {

    @Size(max = 15)
    @Column(name = "UNIPROT_ACCESSION")
    private String uniprotAccession;
    @Size(max = 10)
    @Column(name = "XREF_DB")
    private String xrefDb;
    @Size(max = 15)
    @Column(name = "XREF_TYPE")
    private String xrefType;
    @Size(max = 50)
    @Column(name = "XREF")
    private String xref;
    @Size(max = 50)
    @Column(name = "REACTION_DIRECTION")
    private String reactionDirection;
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "REACTION_INFO_INTERNAL_ID")
    private Long reactionInfoInternalId;

    public EnzymeReactionInfo() {
    }

    public EnzymeReactionInfo(Long reactionInfoInternalId) {
        this.reactionInfoInternalId = reactionInfoInternalId;
    }

    public String getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(String uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    public String getXrefDb() {
        return xrefDb;
    }

    public void setXrefDb(String xrefDb) {
        this.xrefDb = xrefDb;
    }

    public String getXrefType() {
        return xrefType;
    }

    public void setXrefType(String xrefType) {
        this.xrefType = xrefType;
    }

    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    public String getReactionDirection() {
        return reactionDirection;
    }

    public void setReactionDirection(String reactionDirection) {
        this.reactionDirection = reactionDirection;
    }

    public Long getReactionInfoInternalId() {
        return reactionInfoInternalId;
    }

    public void setReactionInfoInternalId(Long reactionInfoInternalId) {
        this.reactionInfoInternalId = reactionInfoInternalId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reactionInfoInternalId != null ? reactionInfoInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymeReactionInfo)) {
            return false;
        }
        EnzymeReactionInfo other = (EnzymeReactionInfo) object;
        return !((this.reactionInfoInternalId == null && other.reactionInfoInternalId != null) || (this.reactionInfoInternalId != null && !this.reactionInfoInternalId.equals(other.reactionInfoInternalId)));
    }

    @Override
    public String toString() {
        return "EnzymeReactionInfo{" + "uniprotAccession=" + uniprotAccession + ", xrefDb=" + xrefDb + ", xrefType=" + xrefType + ", xref=" + xref + ", reactionDirection=" + reactionDirection + '}';
    }

}
