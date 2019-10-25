package uk.ac.ebi.ep.comparisonservice.domain;

import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;

/**
 * Comparison for pathways. It considers only ID and name.
 *
 * @author Joseph
 * @since 1.1.0
 */
public class PathwayComparison extends AbstractComparison<PathWay> {

    public PathwayComparison(PathWay p1, PathWay p2) {
        compared = new PathWay[]{p1, p2};
        init(p1, p2);
    }

    @Override
    protected void getSubComparisons(PathWay p1, PathWay p2) {
        subComparisons.put("ID", new StringComparison(p1.getId(), p2.getId()));
        subComparisons.put("Name",
                new StringComparison(p1.getName(), p2.getName()));
    }

    @Override
    public String toString() {
        return "Pathway";
    }

}
