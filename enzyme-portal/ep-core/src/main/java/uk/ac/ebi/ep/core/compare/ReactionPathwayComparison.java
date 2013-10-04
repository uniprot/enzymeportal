package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 * Comparison for ReactionPathway objects.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ReactionPathwayComparison
extends AbstractComparison<ReactionPathway> {

    public ReactionPathwayComparison(ReactionPathway rp1, ReactionPathway rp2) {
        compared = new ReactionPathway[] { rp1, rp2 };
        init(rp1, rp2);
    }

    @Override
    protected void getSubComparisons(ReactionPathway rp1, ReactionPathway rp2) {
        subComparisons.put("Reaction", new ReactionComparison(
                rp1.getReaction(), rp2.getReaction()));
        subComparisons.put("Pathways", new ListComparison(
                rp1.getPathways(), rp2.getPathways()));
    }

    @Override
    public String toString() {
        return "Reaction and pathways";
    }

}
