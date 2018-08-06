package uk.ac.ebi.ep.xml.entity.protein;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_REACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalReaction.findAll", query = "SELECT e FROM EnzymePortalReaction e"),
    @NamedQuery(name = "EnzymePortalReaction.findByReactionInternalId", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionInternalId = :reactionInternalId"),
    @NamedQuery(name = "EnzymePortalReaction.findByReactionId", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionId = :reactionId"),
    @NamedQuery(name = "EnzymePortalReaction.findByReactionName", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionName = :reactionName"),
    @NamedQuery(name = "EnzymePortalReaction.findByReactionSource", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionSource = :reactionSource"),
    @NamedQuery(name = "EnzymePortalReaction.findByRelationship", query = "SELECT e FROM EnzymePortalReaction e WHERE e.relationship = :relationship"),
    @NamedQuery(name = "EnzymePortalReaction.findByUrl", query = "SELECT e FROM EnzymePortalReaction e WHERE e.url = :url")})
public class EnzymePortalReaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REACTION_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_REACTION_INTERNAL_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private Long reactionInternalId;
    @Column(name = "REACTION_ID")
    private String reactionId;
    @Column(name = "REACTION_NAME")
    private String reactionName;
    @Column(name = "REACTION_SOURCE")
    private String reactionSource;
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Column(name = "URL")
    private String url;
    @Size(max = 20)
    @Column(name = "KEGG_ID")
    private String keggId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    public EnzymePortalReaction() {
    }

    public EnzymePortalReaction(Long reactionInternalId) {
        this.reactionInternalId = reactionInternalId;
    }

    public Long getReactionInternalId() {
        return reactionInternalId;
    }

    public void setReactionInternalId(Long reactionInternalId) {
        this.reactionInternalId = reactionInternalId;
    }

    public String getReactionId() {
        return reactionId;
    }

    public void setReactionId(String reactionId) {
        this.reactionId = reactionId;
    }

    public String getReactionName() {
        return reactionName;
    }

    public void setReactionName(String reactionName) {
        this.reactionName = reactionName;
    }

    public String getReactionSource() {
        return reactionSource;
    }

    public void setReactionSource(String reactionSource) {
        this.reactionSource = reactionSource;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
        hash += (reactionInternalId != null ? reactionInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymePortalReaction)) {
            return false;
        }
        EnzymePortalReaction other = (EnzymePortalReaction) object;
        if ((this.reactionInternalId == null && other.reactionInternalId != null) || (this.reactionInternalId != null && !this.reactionInternalId.equals(other.reactionInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return reactionName;
    }

    public String getKeggId() {
        return keggId;
    }

    public void setKeggId(String keggId) {
        this.keggId = keggId;
    }

}
