package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "uniprot_id"
})
/**
 *
 * @author Joseph
 */
public class Sequence {

    @JsonProperty("uniprot_id")
    private String uniprotId;

    @JsonProperty("uniprot_id")
    public String getUniprotId() {
        return uniprotId;
    }

    @JsonProperty("uniprot_id")
    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }
}
