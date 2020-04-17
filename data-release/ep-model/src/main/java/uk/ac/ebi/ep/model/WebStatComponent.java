package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
@Entity
@Table(name = "WEB_STAT_COMPONENT")
public class WebStatComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "webStatCompSeqGenerator", sequenceName = "seq_web_stat_comp")
    @GeneratedValue(generator = "webStatCompSeqGenerator", strategy = GenerationType.SEQUENCE)
    private Long internalId;
    @Column(name = "PROTEIN_STRUCTURE")
    private long proteinStructure;
    @Column(name = "DISEASES")
    private long diseases;
    @Column(name = "PATHWAYS")
    private long pathways;
    @Column(name = "CATALYTIC_ACTIVITIES")
    private long catalyticActivities;
    @Column(name = "REACTIONS")
    private long reactions;
    @Column(name = "COFACTORS")
    private long cofactors;
    @Column(name = "INHIBITORS")
    private long inhibitors;
    @Column(name = "ACTIVATORS")
    private long activators;
    @Column(name = "EXP_EVIDENCE")
    private long expEvidence;
    @Column(name = "METABOLITES")
    private long metabolites;
    @Column(name = "REACTION_PARAMETERS")
    private long reactionParameters;
    @Column(name = "REACTION_MECHANISMS")
    private long reactionMechanisms;
    @Column(name = "CITATIONS")
    private long citations;
    @Column(name = "REVIEWED")
    private long reviewed;
    @Column(name = "UNREVIEWED")
    private long unreviewed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "RELEASE_ID")
    private String releaseId;
    @Column(name = "RELEASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    public WebStatComponent() {
    }

    public WebStatComponent(Long internalId) {
        this.internalId = internalId;
    }

    public WebStatComponent(Long internalId, String releaseId) {
        this.internalId = internalId;
        this.releaseId = releaseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WebStatComponent)) {
            return false;
        }
        WebStatComponent other = (WebStatComponent) object;
        return !((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId)));
    }

    @Override
    public String toString() {
        return "WebStatComponent{" + "internalId=" + internalId + ", proteinStructure=" + proteinStructure + ", diseases=" + diseases + ", pathways=" + pathways + ", catalyticActivities=" + catalyticActivities + ", reactions=" + reactions + ", cofactors=" + cofactors + ", inhibitors=" + inhibitors + ", activators=" + activators + ", expEvidence=" + expEvidence + ", metabolites=" + metabolites + ", reactionParameters=" + reactionParameters + ", reactionMechanisms=" + reactionMechanisms + ", citations=" + citations + ", reviewed=" + reviewed + ", unreviewed=" + unreviewed + ", releaseId=" + releaseId + ", releaseDate=" + releaseDate + '}';
    }

}
