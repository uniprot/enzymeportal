package uk.ac.ebi.ep.core.compare;

import org.apache.commons.lang.ObjectUtils;

import uk.ac.ebi.ep.enzyme.model.ProteinStructure;

/**
 * Comparison for wrappers around ProteinStructure objects. This only takes into
 * account the ID of the underlying structures.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class ProteinStructureComparison extends
        AbstractComparison<ProteinStructure> {

    public ProteinStructureComparison(ProteinStructure ps1,
            ProteinStructure ps2) {
        compared = new ProteinStructure[] { ps1, ps2 };
        init(ps1, ps2);
    }

    @Override
    protected void getSubComparisons(ProteinStructure s1, ProteinStructure s2) {
        // No sub-comparisons here, just seeing if they differ:
        differ = !ObjectUtils.equals(s1.getId(), s2.getId());
    }

    @Override
    public String toString() {
        return "Protein structure";
    }

}
