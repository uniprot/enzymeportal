/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.helper;

/**
 *
 * @author joseph
 */
public enum Role {

    SUBSTRATE_OR_PRODUCT("SUBSTRATE_OR_PRODUCT"), COFACTOR("COFACTOR"),
    ACTIVATOR("ACTIVATOR"), INHIBITOR("INHIBITOR"), DRUG("DRUG"), BIOACTIVE("BIOACTIVE");

    private Role(String name) {
        this.roleName = name;
    }

    private final String roleName;

    public String getRoleName() {
        return roleName;
    }


    
    

}
