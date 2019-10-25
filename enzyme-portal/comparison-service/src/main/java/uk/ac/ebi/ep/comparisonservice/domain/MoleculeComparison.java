package uk.ac.ebi.ep.comparisonservice.domain;

import uk.ac.ebi.ep.comparisonservice.model.Compound;



/**
 * Comparison for molecules. It takes into account both the molecule ID and its
 * name.
 * 
 * @author Joseph
 * @since 1.1.0
 */
public class MoleculeComparison extends AbstractComparison<Compound> {

    public MoleculeComparison(Compound m1, Compound m2) {
        compared = new Compound[] { m1, m2 };
        init(m1, m2);
    }

    @Override
    protected void getSubComparisons(Compound m1, Compound m2) {
        subComparisons.put("ID", new StringComparison(m1.getId(), m2.getId()));
        subComparisons.put("Name",
                new StringComparison(m1.getName(), m2.getName()));
    }

    @Override
    public String toString() {
        return "Molecule";
    }

}
