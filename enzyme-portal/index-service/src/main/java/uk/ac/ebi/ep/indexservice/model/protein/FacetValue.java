package uk.ac.ebi.ep.indexservice.model.protein;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "label",
    "value",
    "count"
})

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class FacetValue {

    @JsonProperty("label")
    private String label;
    @JsonProperty("value")
    private String value;
    @JsonProperty("count")
    private Integer count;

}
