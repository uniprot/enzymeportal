package uk.ac.ebi.ep.web;

import java.io.Serializable;

/**
 * Enumeration of attributes (application, session) used in the Enzyme Portal
 * webapp. 
 * @author rafa
 *
 */
public enum Attribute  implements Serializable{
  
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
     * Short list of enzymes ready to be compared or downloaded. Session scope.
     */
    basket;
     private static final Long ID =1L;
}
