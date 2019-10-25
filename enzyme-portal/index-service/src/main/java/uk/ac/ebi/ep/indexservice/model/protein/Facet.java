package uk.ac.ebi.ep.indexservice.model.protein;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "label",
    "total",
    "facetValues"
})

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class Facet {

    @JsonProperty("id")
    private String id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("facetValues")
    private List<FacetValue> facetValues;

    @JsonProperty("facetValues")
    public List<FacetValue> getFacetValues() {
        if (facetValues == null) {
            facetValues = new ArrayList<>();
        }
        return facetValues;
    }

    @JsonProperty("facetValues")
    public void setFacetValues(List<FacetValue> facetValues) {
        this.facetValues = facetValues;
    }

}
