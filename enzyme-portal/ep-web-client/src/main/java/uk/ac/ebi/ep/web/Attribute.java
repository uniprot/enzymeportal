package uk.ac.ebi.ep.web;

/**
 * Enumeration of attributes (application, session) used in the Enzyme Portal
 * webapp. 
 *
 * @author rafa
 *
 */
public enum Attribute {
  
           /**
     * Cache of previous searches made. Application scope.
     */
    prevSearches("prevSearches"),
    /**
     * Search history. Session scope.
     */
    history("history"),
    /**
     * Last summaries shown to the user. Session scope.
     */
    lastSummaries("lastSummaries"),
    /**
     * Short list of enzymes ready to be compared or downloaded. Session scope.
     */
    basket("basket");
    private final String name;

    private Attribute(String value) {
        this.name = value;
}

    public String getName() {
        return name;
    }
}
