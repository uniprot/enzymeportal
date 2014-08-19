package uk.ac.ebi.ep.base.comparison;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import uk.ac.ebi.ep.data.enzyme.model.ProteinStructure;



/**
 * Comparison for wrappers around ProteinStructure objects. This only takes into
 * account the ID of the underlying structures.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ProteinStructureComparison
extends AbstractComparison<List<ProteinStructure>> {

    @SuppressWarnings("unchecked")
    public ProteinStructureComparison(List<ProteinStructure> ps1,
            List<ProteinStructure> ps2) {
        compared = new List[]{ ps1, ps2 };
        init(ps1, ps2);
    }

    @Override
    protected void getSubComparisons(List<ProteinStructure> s1,
            List<ProteinStructure> s2) {
        // No sub-comparisons here.
        differ = !CollectionUtils.isEmpty(s1) || !CollectionUtils.isEmpty(s2);
    }

    @Override
    public String toString() {
        return "Protein structure";
    }

}
