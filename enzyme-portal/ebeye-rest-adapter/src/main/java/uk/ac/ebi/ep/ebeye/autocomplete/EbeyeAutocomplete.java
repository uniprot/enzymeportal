/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.autocomplete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeAutocomplete {

    @JsonProperty("suggestions")
    private List<Suggestion> suggestions;

    public final List<Suggestion> getSuggestions() {
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        return suggestions;
    }
 
}
