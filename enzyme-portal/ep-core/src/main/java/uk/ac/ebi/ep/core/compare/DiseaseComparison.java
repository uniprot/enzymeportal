package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.Disease;

/**
 * Comparison for diseases. It only takes into account the disease ID and name.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class DiseaseComparison extends AbstractComparison<Disease> {

    public DiseaseComparison(Disease d1, Disease d2) {
        compared = new Disease[] { d1, d2 };
        init(d1, d2);
    }

    @Override
    protected void getSubComparisons(Disease d1, Disease d2) {
        subComparisons.put("ID", new StringComparison(d1.getId(), d2.getId()));
        subComparisons.put("Name",
                new StringComparison(d1.getName(), d2.getName()));
    }

    @Override
    public String toString() {
        return "Diseases";
    }

}
