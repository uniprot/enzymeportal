package uk.ac.ebi.ep.pdbeadapter.molecule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 *
 * @author joseph
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "residue_number"
})
public class End {

    @JsonProperty("residue_number")
    private Integer residueNumber;

    /**
     *
     * @return The residueNumber
     */
    @JsonProperty("residue_number")
    public Integer getResidueNumber() {
        return residueNumber;
    }

    /**
     *
     * @param residueNumber The residue_number
     */
    @JsonProperty("residue_number")
    public void setResidueNumber(Integer residueNumber) {
        this.residueNumber = residueNumber;
    }

    @Override
    public String toString() {
        return String.valueOf(residueNumber);
    }

}
