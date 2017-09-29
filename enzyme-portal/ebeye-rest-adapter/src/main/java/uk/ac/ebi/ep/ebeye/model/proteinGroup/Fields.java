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

    @JsonProperty("entry_type")
    private List<String> entryType = new ArrayList<>();

    @JsonProperty("gene_name")
    private List<String> geneName = new ArrayList<>();

    @JsonProperty("primary_image")
    private List<String> primaryImage = new ArrayList<>();

    @JsonProperty("function")
    private List<String> function = new ArrayList<>();

    @JsonProperty("related_species")
    private List<String> relatedSpecies = new ArrayList<>();
    @JsonProperty("synonym")
    private List<String> synonym = new ArrayList<>();
    @JsonProperty("disease_name")
    private List<String> diseaseName = new ArrayList<>();

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

    @JsonProperty("entry_type")
    public List<String> getEntryType() {
        return entryType;
    }

    @JsonProperty("entry_type")
    public void setEntryType(List<String> entryType) {
        this.entryType = entryType;
    }

    @JsonProperty("gene_name")
    public List<String> getGeneName() {
        return geneName;
    }

    @JsonProperty("gene_name")
    public void setGeneName(List<String> geneName) {
        this.geneName = geneName;
    }

    @JsonProperty("primary_image")
    public List<String> getPrimaryImage() {
        return primaryImage;
    }

    @JsonProperty("primary_image")
    public void setPrimaryImage(List<String> primaryImage) {
        this.primaryImage = primaryImage;
    }

    @JsonProperty("related_species")
    public List<String> getRelatedSpecies() {
        return relatedSpecies;
    }

    @JsonProperty("related_species")
    public void setRelatedSpecies(List<String> relatedSpecies) {
        this.relatedSpecies = relatedSpecies;
    }

    @JsonProperty("function")
    public List<String> getFunction() {
        return function;
    }

    @JsonProperty("function")
    public void setFunction(List<String> function) {
        this.function = function;
    }

    @JsonProperty("synonym")
    public List<String> getSynonym() {
        return synonym;
    }

    @JsonProperty("synonym")
    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    @JsonProperty("disease_name")
    public List<String> getDiseaseName() {
        return diseaseName;
    }

    @JsonProperty("disease_name")
    public void setDiseaseName(List<String> diseaseName) {
        this.diseaseName = diseaseName;
    }

}
