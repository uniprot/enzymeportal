package uk.ac.ebi.ep.indexservice.model.autocomplete;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Singular;

/**
 *
 * @author Joseph
 */
@Data
public class Autocomplete {
    @Singular
    @JsonProperty("suggestions")
    private final List<Suggestion> suggestions = new ArrayList<>();

   

}
