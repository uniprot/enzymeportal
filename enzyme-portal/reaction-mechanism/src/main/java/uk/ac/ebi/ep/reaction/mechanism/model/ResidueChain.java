package uk.ac.ebi.ep.reaction.mechanism.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "chain_name",
    "pdb_id",
    "code",
    "resid",
    "auth_resid",
    "is_reference",
    "domain"
})

/**
 *
 * @author Joseph
 */
public class ResidueChain {

    @JsonProperty("chain_name")
    private String chainName;
    @JsonProperty("pdb_id")
    private String pdbId;
    @JsonProperty("code")
    private String code;
    @JsonProperty("resid")
    private Integer resid;
    @JsonProperty("auth_resid")
    private Integer authResid;
    @JsonProperty("is_reference")
    private Boolean isReference;
    @JsonProperty("domain")
    private Domain domain;

    @JsonProperty("chain_name")
    public String getChainName() {
        return chainName;
    }

    @JsonProperty("chain_name")
    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    @JsonProperty("pdb_id")
    public String getPdbId() {
        return pdbId;
    }

    @JsonProperty("pdb_id")
    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

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

    @JsonProperty("auth_resid")
    public Integer getAuthResid() {
        return authResid;
    }

    @JsonProperty("auth_resid")
    public void setAuthResid(Integer authResid) {
        this.authResid = authResid;
    }

    @JsonProperty("is_reference")
    public Boolean getIsReference() {
        return isReference;
    }

    @JsonProperty("is_reference")
    public void setIsReference(Boolean isReference) {
        this.isReference = isReference;
    }

    @JsonProperty("domain")
    public Domain getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
