package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "mcsa_id",
    "enzyme_name",
    "url",
    "description",
    "protein",
    "all_ecs",
    "residues",
    "reaction"
})
/**
 *
 * @author Joseph
 */
public class Result {

//    @JsonProperty("mcsa_id")
//    private Integer mcsaId;
//    @JsonProperty("enzyme_name")
//    private String enzymeName;
//    @JsonProperty("url")
//    private String url;
//    @JsonProperty("description")
//    private String description;
//    @JsonProperty("protein")
//    private Protein protein;
//    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
//    @JsonProperty("all_ecs")
//    private List<String> allEcs;// = new ArrayList<String>();
//    //@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
//    @JsonProperty("residues")
//    private List<Residue> residues;// = new ArrayList<Residue>();
//    @JsonProperty("reaction")
//    private Reaction reaction;
    @JsonProperty("mcsa_id")
    private Integer mcsaId;
    @JsonProperty("enzyme_name")
    private String enzymeName;
    @JsonProperty("url")
    private String url;
    @JsonProperty("description")
    private String description;
    @JsonProperty("protein")
    private Protein protein;
    //@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    //@JsonDeserialize(as = String.class)
    @JsonProperty("all_ecs")
   // private String allEcs;
    private List<String> allEcs;// = new ArrayList<String>();
    @JsonProperty("residues")
    private List<Residue> residues;// = new ArrayList<>();
    @JsonProperty("reaction")
    private Reaction reaction;

    @JsonProperty("mcsa_id")
    public Integer getMcsaId() {
        return mcsaId;
    }

    @JsonProperty("mcsa_id")
    public void setMcsaId(Integer mcsaId) {
        this.mcsaId = mcsaId;
    }

    @JsonProperty("enzyme_name")
    public String getEnzymeName() {
        return enzymeName;
    }

    @JsonProperty("enzyme_name")
    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("protein")
    public Protein getProtein() {
        return protein;
    }

    @JsonProperty("protein")
    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    @JsonProperty("all_ecs")
    public List<String> getAllEcs() {
        if (allEcs == null) {
            allEcs = new ArrayList<>();
        }
        return allEcs;
    }

    @JsonProperty("all_ecs")
    public void setAllEcs(List<String> allEcs) {
        this.allEcs = allEcs;
    }
//    @JsonProperty("all_ecs")
//    public String getAllEcs() {
//        return allEcs;
//    }
//
//    @JsonProperty("all_ecs")
//    public void setAllEcs(String allEcs) {
//        this.allEcs = allEcs;
//    }

    @JsonProperty("residues")
    public List<Residue> getResidues() {
        if (residues == null) {
            residues = new ArrayList<>();
        }
        return residues;
    }

    @JsonProperty("residues")
    public void setResidues(List<Residue> residues) {
        this.residues = residues;
    }

    @JsonProperty("reaction")
    public Reaction getReaction() {
        return reaction;
    }

    @JsonProperty("reaction")
    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.mcsaId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Result other = (Result) obj;
        if (!Objects.equals(this.mcsaId, other.mcsaId)) {
            return false;
        }
        return true;
    }

}
