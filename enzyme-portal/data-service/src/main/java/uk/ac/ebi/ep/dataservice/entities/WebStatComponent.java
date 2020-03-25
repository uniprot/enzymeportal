package uk.ac.ebi.ep.dataservice.entities;

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
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
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


}
