package uk.ac.ebi.ep.comparisonservice.domain;

import uk.ac.ebi.ep.comparisonservice.model.Reaction;





/**
 * Comparison for reactions. It considers reaction ID and equation.
 * 
 * @author Joseph
 * @since 1.1.0
 */
public class ReactionComparison extends AbstractComparison<Reaction> {

    public ReactionComparison(Reaction r1, Reaction r2) {
        compared = new Reaction[] { r1, r2 };
        init(r1, r2);
    }

    @Override
    protected void getSubComparisons(Reaction r1, Reaction r2) {
        subComparisons.put("ID", new StringComparison(r2.getId(), r2.getId()));
        subComparisons.put("Equation", new StringComparison(
                r1.getName(), r2.getName()));
    }

    @Override
    public String toString() {
        return "Reaction";
    }

}
