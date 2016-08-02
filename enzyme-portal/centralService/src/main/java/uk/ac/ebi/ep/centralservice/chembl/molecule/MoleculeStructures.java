package uk.ac.ebi.ep.centralservice.chembl.molecule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "canonical_smiles",
    "standard_inchi",
    "standard_inchi_key"
})
/**
 *
 * @author joseph
 */
public class MoleculeStructures {

    @JsonProperty("canonical_smiles")
    private String canonicalSmiles;
    @JsonProperty("standard_inchi")
    private String standardInchi;
    @JsonProperty("standard_inchi_key")
    private String standardInchiKey;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The canonicalSmiles
     */
    @JsonProperty("canonical_smiles")
    public String getCanonicalSmiles() {
        return canonicalSmiles;
    }

    /**
     *
     * @param canonicalSmiles The canonical_smiles
     */
    @JsonProperty("canonical_smiles")
    public void setCanonicalSmiles(String canonicalSmiles) {
        this.canonicalSmiles = canonicalSmiles;
    }

    /**
     *
     * @return The standardInchi
     */
    @JsonProperty("standard_inchi")
    public String getStandardInchi() {
        return standardInchi;
    }

    /**
     *
     * @param standardInchi The standard_inchi
     */
    @JsonProperty("standard_inchi")
    public void setStandardInchi(String standardInchi) {
        this.standardInchi = standardInchi;
    }

    /**
     *
     * @return The standardInchiKey
     */
    @JsonProperty("standard_inchi_key")
    public String getStandardInchiKey() {
        return standardInchiKey;
    }

    /**
     *
     * @param standardInchiKey The standard_inchi_key
     */
    @JsonProperty("standard_inchi_key")
    public void setStandardInchiKey(String standardInchiKey) {
        this.standardInchiKey = standardInchiKey;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
