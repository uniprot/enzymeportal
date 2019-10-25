package uk.ac.ebi.ep.comparisonservice.domain;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import uk.ac.ebi.ep.dataservice.dto.PdbView;

/**
 * Comparison for wrappers around ProteinStructure objects. This only takes into
 * account the ID of the underlying structures.
 *
 * @author Joseph
 * @since 1.1.0
 */
public class ProteinStructureComparison
        extends AbstractComparison<List<PdbView>> {

    @SuppressWarnings("unchecked")
    public ProteinStructureComparison(List<PdbView> ps1,
            List<PdbView> ps2) {
        compared = new List[]{ps1, ps2};
        init(ps1, ps2);
    }

    @Override
    protected void getSubComparisons(List<PdbView> s1,List<PdbView> s2) {
        // No sub-comparisons here.
        differ = !CollectionUtils.isEmpty(s1) || !CollectionUtils.isEmpty(s2);
    }

    @Override
    public String toString() {
        return "Protein structure";
    }

}
