package uk.ac.ebi.ep.centralservice.helper;

import uk.ac.ebi.ep.model.search.model.Compound;
import uk.ac.ebi.ep.model.EnzymePortalCompound;

/**
 *
 * @author joseph
 */
public class CompoundUtil {

    private CompoundUtil() {
    }

    public static EnzymePortalCompound computeRole(EnzymePortalCompound compound, String relationship) {
        switch (Relationship.valueOf(relationship)) {
            case is_reactant_or_product_of:
            case is_substrate_or_product_of:
            case is_reactant_of:
            case is_substrate_of:
            case is_product_of:
                compound.setCompoundRole(Compound.Role.SUBSTRATE_OR_PRODUCT.name());
                break;
            case is_cofactor_of:
                compound.setCompoundRole(Compound.Role.COFACTOR.name());
                break;
            case is_activator_of:
                compound.setCompoundRole(Compound.Role.ACTIVATOR.name());
                break;
            case is_inhibitor_of:
                compound.setCompoundRole(Compound.Role.INHIBITOR.name());
                break;
            case is_drug_for:
            case is_target_of:
                compound.setCompoundRole(compound.getCompoundId().startsWith("CHEMBL")
                        ? Compound.Role.BIOACTIVE.name() : Compound.Role.DRUG.name());
                break;
            default:
        }
        return compound;
    }

    public static String computeRole(String compoundId, String relationship) {
        String role = relationship;
        switch (Relationship.valueOf(relationship)) {
            case is_reactant_or_product_of:
            case is_substrate_or_product_of:
            case is_reactant_of:
            case is_substrate_of:
            case is_product_of:
                role = Compound.Role.SUBSTRATE_OR_PRODUCT.name();
                break;
            case is_cofactor_of:
                role = Compound.Role.COFACTOR.name();
                break;
            case is_activator_of:
                role = Compound.Role.ACTIVATOR.name();
                break;
            case is_inhibitor_of:
                role = Compound.Role.INHIBITOR.name();
                break;
            case is_drug_for:
            case is_target_of:
                role = compoundId.startsWith("CHEMBL") ? Compound.Role.BIOACTIVE.name() : Compound.Role.DRUG.name();
                break;
            default:
        }
        return role;
    }
}
