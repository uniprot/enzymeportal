package uk.ac.ebi.ep.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ec",
    "mechanisms"
})
/**
 *
 * @author Joseph
 */
public class Reaction {

    @JsonProperty("ec")
    private String ec;
    @JsonProperty("mechanisms")
    private List<Mechanism> mechanisms = new ArrayList<>();

    @JsonProperty("ec")
    public String getEc() {
        return ec;
    }

    @JsonProperty("ec")
    public void setEc(String ec) {
        this.ec = ec;
    }

    @JsonProperty("mechanisms")
    public List<Mechanism> getMechanisms() {
        return mechanisms;
    }

    @JsonProperty("mechanisms")
    public void setMechanisms(List<Mechanism> mechanisms) {
        this.mechanisms = mechanisms;
    }
}
