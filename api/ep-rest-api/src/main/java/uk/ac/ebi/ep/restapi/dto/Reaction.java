package uk.ac.ebi.ep.restapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;

/**
 *
 * @author joseph
 */
@Value
@Builder
public class Reaction {

    private ReactionParameter reactionParameter;
    @Singular
    private List<EnzymeReactionView> rheaReactions;
    private MechanismResult reactionMechanism;
    @Singular
    private List<String> catalyticActivities;

}
