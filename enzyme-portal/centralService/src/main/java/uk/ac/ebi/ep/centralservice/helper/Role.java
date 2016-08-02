package uk.ac.ebi.ep.centralservice.helper;

/**
 *
 * @author joseph
 */
public enum Role {

    SUBSTRATE_OR_PRODUCT("SUBSTRATE_OR_PRODUCT"), COFACTOR("COFACTOR"),
    ACTIVATOR("ACTIVATOR"), INHIBITOR("INHIBITOR"), DRUG("DRUG"), BIOACTIVE("BIOACTIVE");
    private final String roleName;

    private Role(String name) {
        this.roleName = name;
    }

    public String getRoleName() {
        return roleName;
    }

}
