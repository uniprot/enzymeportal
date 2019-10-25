package uk.ac.ebi.ep.dataservice.common;

/**
 *
 * @author joseph
 */
public enum CompoundRole {
    COFACTOR("COFACTOR"), INHIBITOR("INHIBITOR"), ACTIVATOR("ACTIVATOR");
    private final String name;

    private CompoundRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
