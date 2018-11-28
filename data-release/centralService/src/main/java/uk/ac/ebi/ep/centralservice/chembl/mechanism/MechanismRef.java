package uk.ac.ebi.ep.centralservice.chembl.mechanism;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ref_id",
    "ref_type",
    "ref_url"
})
/**
 *
 * @author Joseph
 */
public class MechanismRef {

    @JsonProperty("ref_id")
    private String refId;
    @JsonProperty("ref_type")
    private String refType;
    @JsonProperty("ref_url")
    private String refUrl;

    @JsonProperty("ref_id")
    public String getRefId() {
        return refId;
    }

    @JsonProperty("ref_id")
    public void setRefId(String refId) {
        this.refId = refId;
    }

    @JsonProperty("ref_type")
    public String getRefType() {
        return refType;
    }

    @JsonProperty("ref_type")
    public void setRefType(String refType) {
        this.refType = refType;
    }

    @JsonProperty("ref_url")
    public String getRefUrl() {
        return refUrl;
    }

    @JsonProperty("ref_url")
    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }
}
