package uk.ac.ebi.ep.restapi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;

/**
 *
 * @author joseph
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    private ReactionParameter reactionParameter;
    @Singular
    private List<EnzymeReactionView> rheaReactions;
    private MechanismResult reactionMechanism;
    @Singular
    private List<String> catalyticActivities;

}
