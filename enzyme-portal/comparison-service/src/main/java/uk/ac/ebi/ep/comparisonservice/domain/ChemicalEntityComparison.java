package uk.ac.ebi.ep.comparisonservice.domain;

import uk.ac.ebi.ep.comparisonservice.model.Molecule;

/**
 * Comparison for chemical entities related to an enzyme. This includes
 * activators, inhibitors, cofactors, drugs and bioactive ligands 
 *
 * @author Joseph
 * @since 1.1.0
 */
public class ChemicalEntityComparison
        extends AbstractComparison<Molecule> {

    public ChemicalEntityComparison(Molecule ce1, Molecule ce2) {
        compared = new Molecule[]{ce1, ce2};
        init(ce1, ce2);
    }

    @Override
    protected void getSubComparisons(Molecule ce1, Molecule ce2) {
        if (ce1.getActivators() != null || ce2.getActivators() != null) {
            subComparisons.put("Activators", new ListComparison(
                    ce1.getActivators() == null
                    ? null : ce1.getActivators(),
                    ce2.getActivators() == null
                    ? null : ce2.getActivators()));
        }
        if (ce1.getInhibitors() != null || ce2.getInhibitors() != null) {
            subComparisons.put("Inhibitors", new ListComparison(
                    ce1.getInhibitors() == null
                    ? null : ce1.getInhibitors(),
                    ce2.getInhibitors() == null
                    ? null : ce2.getInhibitors()));
        }
        if (ce1.getCofactors() != null || ce2.getCofactors() != null) {
            subComparisons.put("Cofactors", new ListComparison(
                    ce1.getCofactors() == null
                    ? null : ce1.getCofactors(),
                    ce2.getCofactors() == null
                    ? null : ce2.getCofactors()));
        }
    }

    @Override
    public String toString() {
        return "Small molecules";
    }

}
