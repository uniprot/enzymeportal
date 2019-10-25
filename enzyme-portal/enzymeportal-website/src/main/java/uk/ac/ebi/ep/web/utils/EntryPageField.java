package uk.ac.ebi.ep.common;

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
    REACTIONSPATHWAYS("reactionsPathways"),
        /**
     * Reactions/pathways tab.
     */
    REACTIONSMECHANISMS("reactionsMechanisms"),
        /**
     * Reactions/pathways tab.
     */
    PATHWAYS("pathways"),
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
