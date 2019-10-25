package uk.ac.ebi.ep.comparisonservice.domain;

import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;

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
 * @author Joseph
 * @since 1.1.0
 */
public class SummaryComparison extends AbstractComparison<ComparisonProteinModel> {

    public SummaryComparison(ComparisonProteinModel e1, ComparisonProteinModel e2) {
        compared = new ComparisonProteinModel[]{e1, e2};
        init(e1, e2);
    }

    @Override
    protected void getSubComparisons(ComparisonProteinModel e1, ComparisonProteinModel e2) {
        subComparisons.put("Name", new StringComparison(
                e1.getProteinName(), e2.getProteinName()));
        StringBuilder sb1
                = new StringBuilder(e1.getSpecies().getScientificname());
        if (e1.getSpecies().getCommonname() != null) {
            sb1.append(")").insert(0, " (")
                    .insert(0, e1.getSpecies().getCommonname());
        }
        StringBuilder sb2
                = new StringBuilder(e2.getSpecies().getScientificname());
        if (e2.getSpecies().getCommonname() != null) {
            sb2.append(")").insert(0, " (")
                    .insert(0, e2.getSpecies().getCommonname());
        }
        subComparisons.put("Species", new StringComparison(
                sb1.toString(), sb2.toString()));
        subComparisons.put("Function", new StringComparison(
                e1.getProteinView().getFunction(), e2.getProteinView().getFunction()));
        subComparisons.put("EC classification", new ListComparison(
                e1.getEc(), e2.getEc()));
        subComparisons.put("Sequence", new SequenceComparison(
                e1.getProteinView().getSequenceLength(), e2.getProteinView().getSequenceLength()));
    }

    @Override
    public String toString() {
        return "Summary";
    }

}
