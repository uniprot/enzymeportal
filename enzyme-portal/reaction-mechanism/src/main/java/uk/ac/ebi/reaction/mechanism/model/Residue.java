package uk.ac.ebi.reaction.mechanism.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mcsa_id",
    "roles_summary",
    "function_location_abv",
    "roles",
    "residue_chains",
    "residue_sequences"
})
/**
 *
 * @author Joseph
 */
public class Residue {

    @JsonProperty("mcsa_id")
    private Integer mcsaId;
    @JsonProperty("roles_summary")
    private String rolesSummary;
    @JsonProperty("function_location_abv")
    private String functionLocationAbv;
    @JsonProperty("roles")
    private List<Role> roles = new ArrayList<>();
    @JsonProperty("residue_chains")
    private List<ResidueChain> residueChains = new ArrayList<>();
    @JsonProperty("residue_sequences")
    private List<ResidueSequence> residueSequences = new ArrayList<>();

    @JsonProperty("mcsa_id")
    public Integer getMcsaId() {
        return mcsaId;
    }

    @JsonProperty("mcsa_id")
    public void setMcsaId(Integer mcsaId) {
        this.mcsaId = mcsaId;
    }

    @JsonProperty("function_location_abv")
    public String getFunctionLocationAbv() {
        return functionLocationAbv;
    }

    @JsonProperty("function_location_abv")
    public void setFunctionLocationAbv(String functionLocationAbv) {
        this.functionLocationAbv = functionLocationAbv;
    }

    @JsonProperty("roles_summary")
    public String getRolesSummary() {
        return rolesSummary;
    }

    @JsonProperty("roles_summary")
    public void setRolesSummary(String rolesSummary) {
        this.rolesSummary = rolesSummary;
    }

    @JsonProperty("roles")
    public List<Role> getRoles() {
        return roles;
    }

    @JsonProperty("roles")
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @JsonProperty("residue_sequences")
    public List<ResidueSequence> getResidueSequences() {
        return residueSequences;
    }

    @JsonProperty("residue_sequences")
    public void setResidueSequences(List<ResidueSequence> residueSequences) {
        this.residueSequences = residueSequences;
    }

    @JsonProperty("residue_chains")
    public List<ResidueChain> getResidueChains() {
        return residueChains;
    }

    @JsonProperty("residue_chains")
    public void setResidueChains(List<ResidueChain> residueChains) {
        this.residueChains = residueChains;
    }
}
