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
 * @author Joseph <joseph@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "label",
    "total",
    "facetValues"
})
@Data
public class Facet {

    @JsonProperty("id")
    private String id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("total")
    private Integer total;
    @Singular
    @JsonProperty("facetValues")
    private List<FacetValue> facetValues = new ArrayList<>();

}
