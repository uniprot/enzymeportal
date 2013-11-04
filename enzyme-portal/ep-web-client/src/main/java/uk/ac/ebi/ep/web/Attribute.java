package uk.ac.ebi.ep.web;

/**
 * Enumeration of attributes (application, session) used in the Enzyme Portal
 * webapp. 
 * @author rafa
 *
 */
public enum Attribute {
    /**
     * Cache of previous searches made. Application scope.
     */
    prevSearches,
    /**
     * Search history. Session scope.
     */
    history,
    /**
     * Last summaries shown to the user. Session scope.
     */
    lastSummaries,
    /**
     * Short list of enzymes ready to be compared or downloaded.
     */
    basket;
}
