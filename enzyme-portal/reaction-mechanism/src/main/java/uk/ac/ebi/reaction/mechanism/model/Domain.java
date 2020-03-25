package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "cath_id"
})
/**
 *
 * @author Joseph
 */
@Data
public class Domain {

    @JsonProperty("name")
    private String name;
    @JsonProperty("cath_id")
    private String cathId;

}
