package uk.ac.ebi.ep.dataservice.dto;

import java.util.Date;
import lombok.Value;
import org.springframework.data.web.ProjectedPayload;

/**
 *
 * @author joseph
 */
@ProjectedPayload
@Value
public class WebComponentDto {

    private long proteinStructure;

    private long diseases;

    private long pathways;

    private long catalyticActivities;

    private long reactions;

    private long cofactors;

    private long inhibitors;

    private long activators;

    private long expEvidence;

    private long metabolites;

    private long reactionParameters;

    private long reactionMechanisms;

    private long citations;

    private long reviewed;

    private long unreviewed;

    private String releaseId;
    private Date releaseDate;
}
