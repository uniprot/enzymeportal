package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

/**
 * Comparison for whole enzymes.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class EnzymeComparison extends AbstractComparison<EnzymeModel> {

    public EnzymeComparison(EnzymeModel e1, EnzymeModel e2) {
        compared = new EnzymeModel[] { e1, e2 };
        subComparisons.add(new SummaryComparison(e1, e2));
        subComparisons.add(new ListComparison(
                e1.getProteinstructure(), e2.getProteinstructure()));
        subComparisons.add(new ListComparison(
                e1.getReactionpathway(), e2.getReactionpathway()));
        subComparisons.add(new ChemicalEntityComparison(
                e1.getMolecule(), e2.getMolecule()));
        subComparisons.add(new ListComparison(
                e1.getDisease(), e2.getDisease()));
        doDiffer();
    }
}
