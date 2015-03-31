package uk.ac.ebi.ep.data.entry;

/**
 * Field to show for an enzyme in the portal.
 *
 * @author rafa
 *
 */
public enum Field {

    /**
     * The minimal information to handle result lists and filters internally.
     */
    /**
     * The minimal information to handle result lists and filters internally.
     */
    /**
     * The minimal information to handle result lists and filters internally.
     */
    /**
     * The minimal information to handle result lists and filters internally.
     */
    MINIMAL("minimal"),
    /**
     * Just a summary to show in the results page.
     */
    BRIEF("brief"),
    /**
     * Enzyme tab.
     */
    ENZYME("enzyme"),
    /**
     * Structure tab.
     */
    PROTEINSTRUCTURE("proteinStructure"),
    /**
     * Reactions/pathways tab.
     */
    REACTIONPATHWAYS("reactionsPathways"),
    /**
     * Small molecules tab.
     */
    MOLECULES("molecules"),
    /**
     * Diseases/drugs tab.
     */
    DISEASEDRUGS("diseaseDrugs"),
    /**
     * Literature tab.
     */
    LITERATURE("literature"),
    /**
     * Full information about the enzyme.
     */
    FULL("full");
    private final String name;

    private Field(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
