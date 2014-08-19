/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;

/**
 *
 * @author joseph
 */
public class SearchModel implements Serializable {
    
    
   
    protected SearchParams searchparams;
   
    protected SearchResults searchresults;

    /**
     * Gets the value of the searchparams property.
     * 
     * @return
     *     possible object is
     *     {@link SearchParams }
     *     
     */
    public SearchParams getSearchparams() {
        return searchparams;
    }

    /**
     * Sets the value of the searchparams property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchParams }
     *     
     */
    public void setSearchparams(SearchParams value) {
        this.searchparams = value;
    }

    /**
     * Gets the value of the searchresults property.
     * 
     * @return
     *     possible object is
     *     {@link SearchResults }
     *     
     */
    public SearchResults getSearchresults() {
        return searchresults;
    }

    /**
     * Sets the value of the searchresults property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchResults }
     *     
     */
    public void setSearchresults(SearchResults value) {
        this.searchresults = value;
    }


    public SearchModel withSearchparams(SearchParams value) {
        setSearchparams(value);
        return this;
    }

    public SearchModel withSearchresults(SearchResults value) {
        setSearchresults(value);
        return this;
    }

}
