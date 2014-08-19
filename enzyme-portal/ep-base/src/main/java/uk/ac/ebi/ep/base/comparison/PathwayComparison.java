package uk.ac.ebi.ep.base.comparison;

import uk.ac.ebi.ep.data.enzyme.model.Pathway;



/**
 * Comparison for pathways. It considers only ID and name.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class PathwayComparison extends AbstractComparison<Pathway> {

    public PathwayComparison(Pathway p1, Pathway p2) {
        compared = new Pathway[] { p1, p2 };
        init(p1, p2);
    }

    @Override
    protected void getSubComparisons(Pathway p1, Pathway p2) {
        subComparisons.put("ID", new StringComparison(p1.getId(), p2.getId()));
        subComparisons.put("Name",
                new StringComparison(p1.getName(), p2.getName()));
    }

    @Override
    public String toString() {
        return "Pathway";
    }

}
