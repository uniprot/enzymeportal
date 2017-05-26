package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//    "id",
//    "name"
//})
public class Fields {

    @JsonProperty("id")
    private List<String> id = new ArrayList<>();
    @JsonProperty("name")
    private List<String> name = new ArrayList<>();
    @JsonProperty("common_name")
    private List<String> commonName = new ArrayList<>();
    @JsonProperty("scientific_name")
    private List<String> scientificName = new ArrayList<>();
    @JsonProperty("UNIPROTKB")
    private List<String> UNIPROTKB = new ArrayList<>();
    @JsonProperty("primary_accession")
    private List<String> primaryAccession = new ArrayList<>();
    @JsonProperty("primary_organism")
    private List<String> primaryOrganism = new ArrayList<>();
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("id")
    public List<String> getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(List<String> id) {
        this.id = id;
    }

    @JsonProperty("name")
    public List<String> getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(List<String> name) {
        this.name = name;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    /**
     *
     * @return The commonName
     */
    @JsonProperty("common_name")
    public List<String> getCommonName() {
        return commonName;
    }

    /**
     *
     * @param commonName The common_name
     */
    @JsonProperty("common_name")
    public void setCommonName(List<String> commonName) {
        this.commonName = commonName;
    }

    /**
     *
     * @return The scientificName
     */
    @JsonProperty("scientific_name")
    public List<String> getScientificName() {
        return scientificName;
    }

    /**
     *
     * @param scientificName The scientific_name
     */
    @JsonProperty("scientific_name")
    public void setScientificName(List<String> scientificName) {
        this.scientificName = scientificName;
    }

    /**
     *
     * @return The uniprot accession
     */
    @JsonProperty("UNIPROTKB")
    public List<String> getUNIPROTKB() {
        return UNIPROTKB;
    }

    /**
     *
     * @param UNIPROTKB The uniprot accession
     */
    @JsonProperty("UNIPROTKB")
    public void setUNIPROTKB(List<String> UNIPROTKB) {
        this.UNIPROTKB = UNIPROTKB;
    }

    @JsonProperty("primary_accession")
    public List<String> getPrimaryAccession() {
        return primaryAccession;
    }

    @JsonProperty("primary_accession")
    public void setPrimaryAccession(List<String> primaryAccession) {
        this.primaryAccession = primaryAccession;
    }

    @JsonProperty("primary_organism")
    public List<String> getPrimaryOrganism() {
        return primaryOrganism;
    }

    @JsonProperty("primary_organism")
    public void setPrimaryOrganism(List<String> primaryOrganism) {
        this.primaryOrganism = primaryOrganism;
    }

}
