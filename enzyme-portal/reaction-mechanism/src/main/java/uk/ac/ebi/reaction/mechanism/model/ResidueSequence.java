package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "uniprot_id",
    "code",
    "is_reference",
    "resid"
})
/**
 *
 * @author Joseph
 */
@Data
public class ResidueSequence {

    @JsonProperty("uniprot_id")
    private String uniprotId;
    @JsonProperty("code")
    private String code;
    @JsonProperty("is_reference")
    private Boolean isReference;
    @JsonProperty("resid")
    private Integer resid;


}
