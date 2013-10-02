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
        if (ps1 != null && ps2 != null) {
            differ = !ObjectUtils.equals(ps1.getId(), ps2.getId());
        } else if (ps1 == null ^ ps2 == null) {
            differ = true;
        }
    }

}
