package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;

/**
 * Comparison for reactions.
 * @author rafa
 * @since 1.1.0
 */
public class ReactionComparison extends AbstractComparison<EnzymeReaction> {
	
	public ReactionComparison(EnzymeReaction r1, EnzymeReaction r2) {
		compared = new EnzymeReaction[]{ r1, r2 };
		subComparisons.add(new StringComparison(
				r1.getEquation().toString(), r2.getEquation().toString()));
		subComparisons.add(new StringComparison(r2.getId(), r2.getId()));
		doDiffer();
	}

}
