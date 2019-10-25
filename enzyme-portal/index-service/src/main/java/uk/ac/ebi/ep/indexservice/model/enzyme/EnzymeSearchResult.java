package uk.ac.ebi.ep.indexservice.model.enzyme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Singular;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hitCount",
    "entries",
    "facets"
})
public class EnzymeSearchResult {

    @JsonProperty("hitCount")
    private Integer hitCount = 0;
    @Singular
    @JsonProperty("entries")
    private final List<EnzymeEntry> entries = new ArrayList<>();
    @Singular
    @JsonProperty("facets")
    private final List<Facet> facets = new ArrayList<>();
}
