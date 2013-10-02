package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 * Comparison for ReactionPathway objects.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ReactionPathwayComparison extends
        AbstractComparison<ReactionPathway> {

    public ReactionPathwayComparison(ReactionPathway rp1, ReactionPathway rp2) {
        compared = new ReactionPathway[] { rp1, rp2 };
        if (rp1 != null && rp2 != null) {
            subComparisons.add(new ReactionComparison(
                    rp1.getReaction(), rp2.getReaction()));
            subComparisons.add(new ListComparison(
                    rp1.getPathways(), rp2.getPathways()));
            doDiffer();
        } else if (rp1 == null ^ rp2 == null) {
            differ = true;
        }
    }

}
