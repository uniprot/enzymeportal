package uk.ac.ebi.ep.enzymeservice.uniprot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

/**
 *
 * @author joseph
 */
public class PhDependence {

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
