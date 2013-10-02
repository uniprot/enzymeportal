package uk.ac.ebi.ep.core.compare;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 * This comparison requires that the items class implements properly the equals
 * and hashCode methods.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ListComparison extends AbstractComparison<List<?>> {

    public ListComparison(List<?> l1, List<?> l2) {
        compared = new List<?>[] { l1, l2 };
        if (l1 != null && l2 != null) {
            Collection<?> common = CollectionUtils.intersection(l1, l2);
            Collection<?> onlyL1 = CollectionUtils.subtract(l1, common);
            Collection<?> onlyL2 = CollectionUtils.subtract(l2, common);
            for (Object o : common) {
                subComparisons.add(getItemComparison(o, o));
            }
            for (Object o : onlyL1) {
                subComparisons.add(getItemComparison(o, null));
            }
            for (Object o : onlyL2) {
                subComparisons.add(getItemComparison(null, o));
            }
            differ = !onlyL1.isEmpty() || !onlyL2.isEmpty();
        } else if (l1 == null ^ l2 == null) {
            differ = true;
        }
    }

    /**
     * Factory method to get a comparison for two objects according to their
     * class.
     * 
     * @param o1
     * @param o2
     * @return an adequate Comparison.
     */
    private Comparison<?> getItemComparison(Object o1, Object o2) {
        Comparison<?> itemComparison = null;
        if (o1.getClass().equals(String.class)) {
            itemComparison = new StringComparison((String) o1, (String) o2);
        } else if (o1.getClass().equals(ProteinStructure.class)) {
            itemComparison = new ProteinStructureComparison(
                    (ProteinStructure) o1, (ProteinStructure) o2);
        } else if (o1.getClass().equals(ReactionPathway.class)) {
            itemComparison = new ReactionPathwayComparison(
                    (ReactionPathway) o1, (ReactionPathway) o2);
        } else if (o1.getClass().equals(Pathway.class)) {
            itemComparison = new PathwayComparison((Pathway) o1, (Pathway) o2);
        } else if (o1.getClass().equals(Molecule.class)) {
            itemComparison = new MoleculeComparison(
                    (Molecule) o1, (Molecule) o2);
        } else if (o1.getClass().equals(Disease.class)) {
            itemComparison = new DiseaseComparison((Disease) o1, (Disease) o2);
        }
        return itemComparison;
    }
}
