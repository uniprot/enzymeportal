/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.molecule;

/**
 *
 * @author joseph
 */
//public class MoleculeSynonym {
//    
//}
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
    "syn_type",
    "synonyms"
})
/**
 *
 * @author joseph
 */
public class MoleculeSynonym {

    @JsonProperty("syn_type")
    private String synType;
    @JsonProperty("synonyms")
    private String synonyms;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The synType
     */
    @JsonProperty("syn_type")
    public String getSynType() {
        return synType;
    }

    /**
     *
     * @param synType The syn_type
     */
    @JsonProperty("syn_type")
    public void setSynType(String synType) {
        this.synType = synType;
    }

    /**
     *
     * @return The synonyms
     */
    @JsonProperty("synonyms")
    public String getSynonyms() {
//        if (synonyms != null && synonyms.contains("|")) {
//            String name = synonyms.split("|")[0];
//            synonyms = name;
//        }

        return synonyms;
    }

    /**
     *
     * @param synonyms The synonyms
     */
    @JsonProperty("synonyms")
    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
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
