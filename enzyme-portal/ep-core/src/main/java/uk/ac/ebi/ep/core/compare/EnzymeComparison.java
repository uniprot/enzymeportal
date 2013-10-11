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
        init(e1, e2);
    }

    @Override
    protected void getSubComparisons(EnzymeModel e1, EnzymeModel e2) {
        subComparisons.put("Summary", new SummaryComparison(e1, e2));
        subComparisons.put("Protein structures", new ProteinStructureComparison(
                e1.getProteinstructure(), e2.getProteinstructure()));
        subComparisons.put("Reactions and pathways", new ListComparison(
                e1.getReactionpathway(), e2.getReactionpathway()));
        subComparisons.put("Small molecules", new ChemicalEntityComparison(
                e1.getMolecule(), e2.getMolecule()));
        subComparisons.put("Diseases", new ListComparison(
                e1.getDisease(), e2.getDisease()));
    }
}
