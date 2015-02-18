/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.helper;

import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.search.model.Compound;


/**
 *
 * @author joseph
 */
public class CompoundUtil {

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
}
