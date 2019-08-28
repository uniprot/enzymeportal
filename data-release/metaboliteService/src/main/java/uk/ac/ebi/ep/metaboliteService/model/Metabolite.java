package uk.ac.ebi.ep.metaboliteService.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "metabolights_id",
    "metabolights_url"
})
public class Metabolite {

    @JsonProperty("metabolights_id")
    private String metabolightsId;
    @JsonProperty("metabolights_url")
    private String metabolightsUrl;

    @JsonProperty("metabolights_id")
    public String getMetabolightsId() {
        return metabolightsId;
    }

    @JsonProperty("metabolights_id")
    public void setMetabolightsId(String metabolightsId) {
        this.metabolightsId = metabolightsId;
    }

    @JsonProperty("metabolights_url")
    public String getMetabolightsUrl() {
        return metabolightsUrl;
    }

    @JsonProperty("metabolights_url")
    public void setMetabolightsUrl(String metabolightsUrl) {
        this.metabolightsUrl = metabolightsUrl;
    }

}
