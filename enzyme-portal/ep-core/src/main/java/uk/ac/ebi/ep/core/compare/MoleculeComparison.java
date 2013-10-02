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
        subComparisons.add(new StringComparison(m1.getId(), m2.getId()));
        subComparisons.add(new StringComparison(m1.getName(), m2.getName()));
        doDiffer();
    }

}
