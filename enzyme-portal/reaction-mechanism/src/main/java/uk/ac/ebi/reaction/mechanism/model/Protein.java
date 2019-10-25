package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sequences"
})
/**
 *
 * @author Joseph
 */
public class Protein {

    @JsonProperty("sequences")
    private List<Sequence> sequences = new ArrayList<Sequence>();

    @JsonProperty("sequences")
    public List<Sequence> getSequences() {
        return sequences;
    }

    @JsonProperty("sequences")
    public void setSequences(List<Sequence> sequences) {
        this.sequences = sequences;
    }
}
