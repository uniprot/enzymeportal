package uk.ac.ebi.ep.comparisonservice.domain;

import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;


/**
 * Comparison for whole enzymes.
 *
 * @author Joseph
 * @since 1.1.0
 */
public class EnzymeComparison extends AbstractComparison<ComparisonProteinModel> {

    public EnzymeComparison(ComparisonProteinModel e1, ComparisonProteinModel e2) {
        compared = new ComparisonProteinModel[]{e1, e2};
        init(e1, e2);
    }

    @Override
    protected void getSubComparisons(ComparisonProteinModel e1, ComparisonProteinModel e2) {
        subComparisons.put("Summary", new SummaryComparison(e1, e2));
        subComparisons.put("Protein structures", new ProteinStructureComparison(
                e1.getProteinstructure(), e2.getProteinstructure()));
        subComparisons.put("Reactions and pathways", new ListComparison(
                e1.getReactionpathway(), e2.getReactionpathway()));
        subComparisons.put("Small molecules", new ChemicalEntityComparison(
                e1.getMolecule(), e2.getMolecule()));
        subComparisons.put("Diseases", getDiseasesComparison(e1, e2));
    }

    /**
     * Removes evidences (they will necessarily be different) from diseases
     * before the comparison, and add them afterwards. This way, equal diseases
     * will be shown side by side (the ListComparison will consider them as
     * common).
     *
     * @param e1
     * @param e2
     * @return
     */
    private ListComparison getDiseasesComparison(ComparisonProteinModel e1, ComparisonProteinModel e2) {
        // Compare:
        return new ListComparison(e1.getDiseases(), e2.getDiseases());

    }
}
