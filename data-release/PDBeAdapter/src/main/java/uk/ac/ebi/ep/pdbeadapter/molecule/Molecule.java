
package uk.ac.ebi.ep.pdbeadapter.molecule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ca_p_only",
    "weight",
    "entity_id",
    "mutation_flag",
    "in_chains",
    "molecule_type",
    "in_struct_asyms",
    "molecule_name",
    "chem_comp_ids",
    "sample_preparation",
    "number_of_copies",
    "sequence",
    "source",
    "length",
    "synonym",
    "gene_name"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Molecule {

    //private static final long serialVersionUID = 1L;
    @JsonProperty("ca_p_only")
    private Boolean caPOnly;
    @JsonProperty("weight")
    private Double weight;
    @JsonProperty("entity_id")
    private Integer entityId;
    @JsonProperty("mutation_flag")
    private Object mutationFlag;
    @JsonProperty("in_struct_asyms")
    private List<String> inStructAsyms = new ArrayList<>();
    @JsonProperty("molecule_name")
    private List<String> moleculeName = new ArrayList<>();
    @JsonProperty("chem_comp_ids")
    private List<String> chemCompIds = new ArrayList<>();
    @JsonProperty("in_chains")
    private List<String> inChains = new ArrayList<>();
    @JsonProperty("molecule_type")
    private String moleculeType;
    @JsonProperty("sample_preparation")
    private String samplePreparation;
    @JsonProperty("number_of_copies")
    private Integer numberOfCopies;
    @JsonProperty("sequence")
    private String sequence;
    @JsonProperty("source")
    private List<Source> source = new ArrayList<>();
    @JsonProperty("synonym")
    private String synonym;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("gene_name")
    private List<String> geneName = new ArrayList<>();
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The caPOnly
     */
    @JsonProperty("ca_p_only")
    public Boolean getCaPOnly() {
        return caPOnly;
    }

    /**
     *
     * @param caPOnly The ca_p_only
     */
    @JsonProperty("ca_p_only")
    public void setCaPOnly(Boolean caPOnly) {
        this.caPOnly = caPOnly;
    }

    /**
     *
     * @return The weight
     */
    @JsonProperty("weight")
    public Double getWeight() {
        return weight;
    }

    /**
     *
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     *
     * @return The entityId
     */
    @JsonProperty("entity_id")
    public Integer getEntityId() {
        return entityId;
    }

    /**
     *
     * @param entityId The entity_id
     */
    @JsonProperty("entity_id")
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    /**
     *
     * @return The mutationFlag
     */
    @JsonProperty("mutation_flag")
    public Object getMutationFlag() {
        return mutationFlag;
    }

    /**
     *
     * @param mutationFlag The mutation_flag
     */
    @JsonProperty("mutation_flag")
    public void setMutationFlag(Object mutationFlag) {
        this.mutationFlag = mutationFlag;
    }

    /**
     *
     * @return The inStructAsyms
     */
    @JsonProperty("in_struct_asyms")
    public List<String> getInStructAsyms() {
        return inStructAsyms;
    }

    /**
     *
     * @param inStructAsyms The in_struct_asyms
     */
    @JsonProperty("in_struct_asyms")
    public void setInStructAsyms(List<String> inStructAsyms) {
        this.inStructAsyms = inStructAsyms;
    }

/**
* 
* @return
* The moleculeName
*/
@JsonProperty("molecule_name")
public List<String> getMoleculeName() {
return moleculeName;
}

/**
* 
* @param moleculeName
* The molecule_name
*/
@JsonProperty("molecule_name")
public void setMoleculeName(List<String> moleculeName) {
this.moleculeName = moleculeName;
}

    /**
     *
     * @return The chemCompIds
     */
    @JsonProperty("chem_comp_ids")
    public List<String> getChemCompIds() {
        return chemCompIds;
    }

    /**
     *
     * @param chemCompIds The chem_comp_ids
     */
    @JsonProperty("chem_comp_ids")
    public void setChemCompIds(List<String> chemCompIds) {
        this.chemCompIds = chemCompIds;
    }

    /**
     *
     * @return The inChains
     */
    @JsonProperty("in_chains")
    public List<String> getInChains() {
        return inChains;
    }

    /**
     *
     * @param inChains The in_chains
     */
    @JsonProperty("in_chains")
    public void setInChains(List<String> inChains) {
        this.inChains = inChains;
    }

    /**
     *
     * @return The moleculeType
     */
    @JsonProperty("molecule_type")
    public String getMoleculeType() {
        return moleculeType;
    }

    /**
     *
     * @param moleculeType The molecule_type
     */
    @JsonProperty("molecule_type")
    public void setMoleculeType(String moleculeType) {
        this.moleculeType = moleculeType;
    }

    /**
     *
     * @return The samplePreparation
     */
    @JsonProperty("sample_preparation")
    public String getSamplePreparation() {
        return samplePreparation;
    }

    /**
     *
     * @param samplePreparation The sample_preparation
     */
    @JsonProperty("sample_preparation")
    public void setSamplePreparation(String samplePreparation) {
        this.samplePreparation = samplePreparation;
    }

    /**
     *
     * @return The numberOfCopies
     */
    @JsonProperty("number_of_copies")
    public Integer getNumberOfCopies() {
        return numberOfCopies;
    }

    /**
     *
     * @param numberOfCopies The number_of_copies
     */
    @JsonProperty("number_of_copies")
    public void setNumberOfCopies(Integer numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    /**
     *
     * @return The sequence
     */
    @JsonProperty("sequence")
    public String getSequence() {
        return sequence;
    }

    /**
     *
     * @param sequence The sequence
     */
    @JsonProperty("sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     *
     * @return The source
     */
    @JsonProperty("source")
    public List<Source> getSource() {
        return source;
    }

    /**
     *
     * @param source The source
     */
    @JsonProperty("source")
    public void setSource(List<Source> source) {
        this.source = source;
    }

    /**
     *
     * @return The length
     */
    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    /**
     *
     * @param length The length
     */
    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     *
     * @return The synonym
     */
    @JsonProperty("synonym")
    public String getSynonym() {
        return synonym;
    }

    /**
     *
     * @param synonym The synonym
     */
    @JsonProperty("synonym")
    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    /**
     *
     * @return The geneName
     */
    @JsonProperty("gene_name")
    public List<String> getGeneName() {
        return geneName;
    }

    /**
     *
     * @param geneName The gene_name
     */
    @JsonProperty("gene_name")
    public void setGeneName(List<String> geneName) {
        this.geneName = geneName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Molecule{" + "inStructAsyms=" + inStructAsyms + ", moleculeName=" + moleculeName + ", chemCompIds=" + chemCompIds + ", inChains=" + inChains + ", moleculeType=" + moleculeType + ", length=" + length + ", geneName=" + geneName + '}';
    }

}
