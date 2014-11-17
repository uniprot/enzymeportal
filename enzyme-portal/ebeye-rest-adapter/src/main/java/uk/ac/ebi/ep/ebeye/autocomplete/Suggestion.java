/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.autocomplete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Suggestion {

    @JsonProperty("suggestion")
    private String suggestion;

    public String getSuggestion() {
        return suggestion;
    }

    @Override
    public String toString() {
        return suggestion;
    }

}
