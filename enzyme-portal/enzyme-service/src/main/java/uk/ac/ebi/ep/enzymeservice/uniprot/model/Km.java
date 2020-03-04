package uk.ac.ebi.ep.enzymeservice.uniprot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author joseph
 */
public class Km {

    @JsonProperty("value")
    private String value;

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }


}
