package uk.ac.ebi.ep.core.compare;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;

import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 * Comparison for lists of items. It does not take into account their order, so
 * for this class <code>[ "1", "2" ]</code> is not different from
 * <code>[ "2", "1" ]</code>.<br>
 * This comparison requires that the items class implements properly the equals
 * and hashCode methods.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ListComparison extends AbstractComparison<List<?>> {

    public ListComparison(final List<?> l1, final List<?> l2) {
        compared = new List<?>[] { l1, l2 };
        init(l1, l2);
    }

    @Override
    protected void getSubComparisons(List<?> l1, List<?> l2) {
        if (l1 == null) l1 = new ArrayList<Object>();
        if (l2 == null) l2 = new ArrayList<Object>();
        List<?> common = ListUtils.intersection(l1, l2);
        List<?> onlyL1 = ListUtils.subtract(l1, common);
        List<?> onlyL2 = ListUtils.subtract(l2, common);
        int c = 0;
        for (Object o : common) {
            subComparisons.put("same-" + c++, getItemComparison(o, o));
        }
        final int min = Math.min(onlyL1.size(), onlyL2.size());
        int i = 0;
        for (; i < min; i++) {
            subComparisons.put("diff-" + i,
                    getItemComparison(onlyL1.get(i), onlyL2.get(i)));
        }
        if (onlyL1.size() > onlyL2.size()){
            for (; i < onlyL1.size(); i++){
                subComparisons.put("diff-" + i,
                        getItemComparison(onlyL1.get(i), null));
            }
        } else if (onlyL1.size() < onlyL2.size()){
            for (; i < onlyL2.size(); i++){
                subComparisons.put("diff-" + i,
                        getItemComparison(null, onlyL2.get(i)));
            }
        }
        differ = !onlyL1.isEmpty() || !onlyL2.isEmpty();
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
        Class<? extends Object> theClass = o1 != null?
                o1.getClass() : o2.getClass();
        if (theClass.equals(String.class)) {
            itemComparison = new StringComparison((String) o1, (String) o2);
        } else if (theClass.equals(ReactionPathway.class)) {
            itemComparison = new ReactionPathwayComparison(
                    (ReactionPathway) o1, (ReactionPathway) o2);
        } else if (theClass.equals(Pathway.class)) {
            itemComparison = new PathwayComparison((Pathway) o1, (Pathway) o2);
        } else if (theClass.equals(Molecule.class)) {
            itemComparison = new MoleculeComparison(
                    (Molecule) o1, (Molecule) o2);
        } else if (theClass.equals(Disease.class)) {
            itemComparison = new DiseaseComparison((Disease) o1, (Disease) o2);
        }
        return itemComparison;
    }

    @Override
    public String toString() {
        return subComparisons == null || subComparisons.isEmpty()? "" :
                subComparisons.entrySet().iterator().next().getValue()
                .toString();
    }
}
