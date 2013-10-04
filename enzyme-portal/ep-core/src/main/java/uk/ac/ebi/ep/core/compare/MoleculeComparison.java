package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.Molecule;

/**
 * Comparison for molecules. It takes into account both the molecule ID and its
 * name.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class MoleculeComparison extends AbstractComparison<Molecule> {

    public MoleculeComparison(Molecule m1, Molecule m2) {
        compared = new Molecule[] { m1, m2 };
        init(m1, m2);
    }

    @Override
    protected void getSubComparisons(Molecule m1, Molecule m2) {
        subComparisons.put("ID", new StringComparison(m1.getId(), m2.getId()));
        subComparisons.put("Name",
                new StringComparison(m1.getName(), m2.getName()));
    }

    @Override
    public String toString() {
        return "Molecule";
    }

}
