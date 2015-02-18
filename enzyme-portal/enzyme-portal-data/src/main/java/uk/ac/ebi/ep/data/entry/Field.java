package uk.ac.ebi.ep.data.entry;

/**
 * Field to show for an enzyme in the portal.
 * @author rafa
 *
 */
public enum Field {
	/**
	 * The minimal information to handle result lists and filters internally.
	 */
    minimal("minimal"),
    /**
     * Just a summary to show in the results page.
     */
    brief("brief"),
    /**
     * Enzyme tab.
     */
    enzyme("enzyme"),
    /**
     * Structure tab.
     */
    proteinStructure("proteinStructure"),
    /**
     * Reactions/pathways tab.
     */
    reactionsPathways("reactionsPathways"),
    /**
     * Small molecules tab.
     */
    molecules("molecules"),
    /**
     * Diseases/drugs tab.
     */
    diseaseDrugs("diseaseDrugs"),
    /**
     * Literature tab.
     */
    literature("literature"),
    /**
     * Full information about the enzyme.
     */
    full("full");
    
    private Field(String name){
        this.name = name;
    }
    private String name;

    public String getName() {
        return name;
    }
    
    
}