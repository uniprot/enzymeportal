package uk.ac.ebi.ep.indexservice.model.autocomplete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author Joseph
 */
@Data
public class Suggestion {

    @JsonProperty("suggestion")
    private String suggestedKeyword;

    public Suggestion() {

    }

    public Suggestion(String suggestedKeyword) {
        this.suggestedKeyword = suggestedKeyword;
    }

}
