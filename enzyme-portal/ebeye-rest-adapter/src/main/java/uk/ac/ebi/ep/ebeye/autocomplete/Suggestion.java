/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.autocomplete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.suggestion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Suggestion other = (Suggestion) obj;
        if (!Objects.equals(this.suggestion, other.suggestion)) {
            return false;
        }
        return true;
    }
    
    

}
