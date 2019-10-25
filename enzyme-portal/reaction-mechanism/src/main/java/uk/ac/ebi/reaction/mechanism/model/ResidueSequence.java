package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
public class ResidueSequence {

    @JsonProperty("uniprot_id")
    private String uniprotId;
    @JsonProperty("code")
    private String code;
    @JsonProperty("is_reference")
    private Boolean isReference;
    @JsonProperty("resid")
    private Integer resid;

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("resid")
    public Integer getResid() {
        return resid;
    }

    @JsonProperty("resid")
    public void setResid(Integer resid) {
        this.resid = resid;
    }

    @JsonProperty("uniprot_id")
    public String getUniprotId() {
        return uniprotId;
    }

    @JsonProperty("uniprot_id")
    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    @JsonProperty("is_reference")
    public Boolean getIsReference() {
        return isReference;
    }

    @JsonProperty("is_reference")
    public void setIsReference(Boolean isReference) {
        this.isReference = isReference;
    }
}
