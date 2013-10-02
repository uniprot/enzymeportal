package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.Pathway;

/**
 * Comparison for pathways.
 * @author rafa
 * @since 1.1.0
 */
public class PathwayComparison extends AbstractComparison<Pathway> {
	
	public PathwayComparison(Pathway p1, Pathway p2) {
		compared = new Pathway[]{ p1, p2 };
		if (p1 != null && p2 != null){
			subComparisons.add(new StringComparison(p1.getName(), p2.getName()));
			subComparisons.add(new StringComparison(p1.getId(), p2.getId()));
			doDiffer();
		} else if (p1 == null ^ p2 == null){
			differ = true;
		}
	}

}
