package uk.ac.ebi.ep.web.utils;

/**
 * EntryPageField to show for an enzyme in the entry page.
 *
 * @author joseph
 *
 */
public enum EntryPageField {

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
     //ENTRY("entry"),//a hack for litemol refresh
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

    private EntryPageField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
