package uk.ac.ebi.ep.base.comparison;

import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;



/**
 * Comparison for reactions. It considers reaction ID and equation.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ReactionComparison extends AbstractComparison<EnzymeReaction> {

    public ReactionComparison(EnzymeReaction r1, EnzymeReaction r2) {
        compared = new EnzymeReaction[] { r1, r2 };
        init(r1, r2);
    }

    @Override
    protected void getSubComparisons(EnzymeReaction r1, EnzymeReaction r2) {
        subComparisons.put("ID", new StringComparison(r2.getId(), r2.getId()));
        subComparisons.put("Equation", new StringComparison(
                r1.getName(), r2.getName()));
    }

    @Override
    public String toString() {
        return "Reaction";
    }

}
