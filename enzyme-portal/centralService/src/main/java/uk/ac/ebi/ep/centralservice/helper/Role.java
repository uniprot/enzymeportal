package uk.ac.ebi.ep.centralservice.helper;

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
