package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "cath_id"
})
/**
 *
 * @author Joseph
 */
public class Domain {

    @JsonProperty("name")
    private String name;
    @JsonProperty("cath_id")
    private String cathId;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("cath_id")
    public String getCathId() {
        return cathId;
    }

    @JsonProperty("cath_id")
    public void setCathId(String cathId) {
        this.cathId = cathId;
    }
}
