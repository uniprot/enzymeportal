package uk.ac.ebi.ep.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ENZYME_PORTAL_REACTANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalReactant.findAll", query = "SELECT e FROM EnzymePortalReactant e"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantInternalId", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantInternalId = :reactantInternalId"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantId", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantId = :reactantId"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantName", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantName = :reactantName"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantSource", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantSource = :reactantSource"),
    @NamedQuery(name = "EnzymePortalReactant.findByRelationship", query = "SELECT e FROM EnzymePortalReactant e WHERE e.relationship = :relationship"),
    @NamedQuery(name = "EnzymePortalReactant.findByUrl", query = "SELECT e FROM EnzymePortalReactant e WHERE e.url = :url"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactantRole", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactantRole = :reactantRole"),
    @NamedQuery(name = "EnzymePortalReactant.findByReactionDirection", query = "SELECT e FROM EnzymePortalReactant e WHERE e.reactionDirection = :reactionDirection")})
public class EnzymePortalReactant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "REACTANT_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "reactantSeqGenerator", sequenceName = "REACTANT_SEQ")
    @GeneratedValue(generator = "reactantSeqGenerator", strategy = GenerationType.AUTO)
    private Long reactantInternalId;
    @Size(max = 30)
    @Column(name = "REACTANT_ID")
    private String reactantId;
    @Size(max = 4000)
    @Column(name = "REACTANT_NAME")
    private String reactantName;
    @Size(max = 30)
    @Column(name = "REACTANT_SOURCE")
    private String reactantSource;
    @Size(max = 30)
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Size(max = 255)
    @Column(name = "URL")
    private String url;
    @Size(max = 30)
    @Column(name = "REACTANT_ROLE")
    private String reactantRole;
    @Size(max = 20)
    @Column(name = "REACTION_DIRECTION")
    private String reactionDirection;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalReactant() {
    }

    public EnzymePortalReactant(Long reactantInternalId) {
        this.reactantInternalId = reactantInternalId;
    }

    public Long getReactantInternalId() {
        return reactantInternalId;
    }

    public void setReactantInternalId(Long reactantInternalId) {
        this.reactantInternalId = reactantInternalId;
    }

    public String getReactantId() {
        return reactantId;
    }

    public void setReactantId(String reactantId) {
        this.reactantId = reactantId;
    }

    public String getReactantName() {
        return reactantName;
    }

    public void setReactantName(String reactantName) {
        this.reactantName = reactantName;
    }

    public String getReactantSource() {
        return reactantSource;
    }

    public void setReactantSource(String reactantSource) {
        this.reactantSource = reactantSource;
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

    public String getReactantRole() {
        return reactantRole;
    }

    public void setReactantRole(String reactantRole) {
        this.reactantRole = reactantRole;
    }

    public String getReactionDirection() {
        return reactionDirection;
    }

    public void setReactionDirection(String reactionDirection) {
        this.reactionDirection = reactionDirection;
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
        hash += (reactantInternalId != null ? reactantInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof EnzymePortalReactant)) {
            return false;
        }
        EnzymePortalReactant other = (EnzymePortalReactant) object;
        return !((this.reactantInternalId == null && other.reactantInternalId != null) || (this.reactantInternalId != null && !this.reactantInternalId.equals(other.reactantInternalId)));
    }

    @Override
    public String toString() {
        return "EnzymePortalReactant{" + "reactantId=" + reactantId + ", reactantName=" + reactantName + ", reactantSource=" + reactantSource + ", relationship=" + relationship + ", url=" + url + ", reactantRole=" + reactantRole + ", reactionDirection=" + reactionDirection + '}';
    }

}
