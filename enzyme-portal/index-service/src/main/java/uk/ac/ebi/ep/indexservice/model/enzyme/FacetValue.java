package uk.ac.ebi.ep.indexservice.model.enzyme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "label",
    "value",
    "count"
})
public class FacetValue {

    @JsonProperty("label")
    private String label;
    @JsonProperty("value")
    private String value;
    @JsonProperty("count")
    private Integer count;

}
