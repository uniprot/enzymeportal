package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

/**
 * Comparison for enzyme summaries. It takes into account:
 * <ul>
 * <li>enzyme name</li>
 * <li>species (just scientific name)</li>
 * <li>function</li>
 * <li>EC numbers</li>
 * <li>sequence</li>
 * </ul>
 * 
 * @author rafa
 * @since 1.1.0
 */
public class SummaryComparison extends AbstractComparison<EnzymeModel> {

    public SummaryComparison(EnzymeModel e1, EnzymeModel e2) {
        compared = new EnzymeModel[] { e1, e2 };
        init(e1, e2);
    }

    @Override
    protected void getSubComparisons(EnzymeModel e1, EnzymeModel e2) {
        subComparisons.put("Name",
                new StringComparison(e1.getName(), e2.getName()));
        String sp1 = new StringBuilder(e1.getSpecies().getCommonname())
                .append(" (").append(e1.getSpecies().getScientificname())
                .append(")").toString();
        String sp2 = new StringBuilder(e2.getSpecies().getCommonname())
            .append(" (").append(e2.getSpecies().getScientificname())
            .append(")").toString();
        subComparisons.put("Species", new StringComparison(sp1, sp2));
        subComparisons.put("Function", new StringComparison(
                e1.getFunction(), e2.getFunction()));
        subComparisons.put("EC classification",
                new ListComparison(e1.getEc(), e2.getEc()));
        subComparisons.put("Sequence", new SequenceComparison(
                e1.getEnzyme().getSequence(), e2.getEnzyme().getSequence()));
    }

    @Override
    public String toString() {
        return "Summary";
    }

}
