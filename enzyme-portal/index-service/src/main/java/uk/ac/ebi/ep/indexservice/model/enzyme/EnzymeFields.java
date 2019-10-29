package uk.ac.ebi.ep.indexservice.model.enzyme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnzymeFields implements Serializable {

    @JsonProperty("name")
    private List<String> name = new ArrayList<>();
    @JsonProperty("description")
    private List<String> description = new ArrayList<>();
    @JsonProperty("UNIPROTKB")
    private List<String> uniprotKB = new ArrayList<>();
    @JsonProperty("protein_name")
    private Set<String> proteinName = new HashSet<>();
    @JsonProperty("common_name")
    private List<String> commonName = new ArrayList<>();
    @JsonProperty("scientific_name")
    private List<String> scientificName = new ArrayList<>();
    @JsonProperty("compound_name")
    private List<String> compoundName = new ArrayList<>();
    @JsonProperty("disease_name")
    private List<String> diseaseName = new ArrayList<>();
    @JsonProperty("enzyme_family")
    private List<String> enzymeFamily = new ArrayList<>();
    @JsonProperty("intenz_cofactors")
    private Set<String> intenzCofactors = new HashSet<>();
    @JsonProperty("alt_names")
    private Set<String> altNames = new HashSet<>();
    @JsonProperty("status")
    private List<String> status = new ArrayList<>();
    @JsonProperty("catalytic_activity")
    private List<String> catalyticActivity = new ArrayList<>();


}
