package uk.ac.ebi.ep.core.compare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiKeyMap;

import uk.ac.ebi.ep.enzyme.model.Disease;
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
        subComparisons.put("Diseases", getDiseasesComparison(e1, e2));
    }

    /** 
     * Removes evidences (they will necessarily be different) from diseases
     * before the comparison, and add them afterwards. This way, equal
     * diseases will be shown side by side (the ListComparison will consider
     * them as common).
     * @param e1
     * @param e2
     * @return
     */
    private ListComparison getDiseasesComparison(EnzymeModel e1, EnzymeModel e2) {
        // Put evidences away:
        Map<String, List<String>> evs = new HashMap<String, List<String>>();
        final List<Disease> d1 = e1.getDisease();
        for (Disease dis : d1) {
            final List<String> ev = dis.getEvidence();
            dis.setEvidence(null);
            evs.put(e1.getUniprotid() + dis.getId(), ev);
        }
        final List<Disease> d2 = e2.getDisease();
        for (Disease dis : d2) {
            final List<String> ev = dis.getEvidence();
            dis.setEvidence(null);
            evs.put(e2.getUniprotid() + dis.getId(), ev);
        }
        // Compare:
        final ListComparison comparison = new ListComparison(d1, d2);
        // Restore evidences:
        for (Disease dis : d1) {
            dis.setEvidence(evs.get(e1.getUniprotid() + dis.getId()));
        }
        for (Disease dis : d2) {
            dis.setEvidence(evs.get(e2.getUniprotid() + dis.getId()));
        }
        return comparison;
    }
}
