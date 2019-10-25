package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ENZYME_PORTAL_REACTION")
@XmlRootElement
@NamedQuery(name = "EnzymePortalReaction.findAll", query = "SELECT e FROM EnzymePortalReaction e")
@NamedQuery(name = "EnzymePortalReaction.findByReactionInternalId", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionInternalId = :reactionInternalId")
@NamedQuery(name = "EnzymePortalReaction.findByReactionId", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionId = :reactionId")
@NamedQuery(name = "EnzymePortalReaction.findByReactionName", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionName = :reactionName")
@NamedQuery(name = "EnzymePortalReaction.findByReactionSource", query = "SELECT e FROM EnzymePortalReaction e WHERE e.reactionSource = :reactionSource")
@NamedQuery(name = "EnzymePortalReaction.findByRelationship", query = "SELECT e FROM EnzymePortalReaction e WHERE e.relationship = :relationship")
@NamedQuery(name = "EnzymePortalReaction.findByUrl", query = "SELECT e FROM EnzymePortalReaction e WHERE e.url = :url")
@NamedQuery(name = "EnzymePortalReaction.findByKeggId", query = "SELECT e FROM EnzymePortalReaction e WHERE e.keggId = :keggId")
public class EnzymePortalReaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "REACTION_INTERNAL_ID")
    private BigDecimal reactionInternalId;
    @Size(max = 255)
    @Column(name = "REACTION_ID")
    private String reactionId;
    @Size(max = 4000)
    @Column(name = "REACTION_NAME")
    private String reactionName;
    @Size(max = 30)
    @Column(name = "REACTION_SOURCE")
    private String reactionSource;
    @Size(max = 30)
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Size(max = 255)
    @Column(name = "URL")
    private String url;
    @Size(max = 20)
    @Column(name = "KEGG_ID")
    private String keggId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.reactionInternalId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymePortalReaction other = (EnzymePortalReaction) obj;
        return Objects.equals(this.reactionInternalId, other.reactionInternalId);
    }

}
