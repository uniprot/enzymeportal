package uk.ac.ebi.ep.pdbeadapter.cofactor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "chem_comp_name",
    "entity_id",
    "residue_number",
    "author_residue_number",
    "chain_id",
    "alternate_conformers",
    "author_insertion_code",
    "chem_comp_id",
    "class",
    "struct_asym_id"
})
/**
 *
 * @author Joseph
 */
public class Cofactor {

    @JsonProperty("chem_comp_name")
    private String chemCompName;
    @JsonProperty("entity_id")
    private Integer entityId;
    @JsonProperty("residue_number")
    private Integer residueNumber;
    @JsonProperty("author_residue_number")
    private Integer authorResidueNumber;
    @JsonProperty("chain_id")
    private String chainId;
    @JsonProperty("alternate_conformers")
    private Integer alternateConformers;
    @JsonProperty("author_insertion_code")
    private String authorInsertionCode;
    @JsonProperty("chem_comp_id")
    private String chemCompId;
    @JsonProperty("class")
    private String _class;
    @JsonProperty("struct_asym_id")
    private String structAsymId;

    @JsonProperty("chem_comp_name")
    public String getChemCompName() {
        return chemCompName;
    }

    @JsonProperty("chem_comp_name")
    public void setChemCompName(String chemCompName) {
        this.chemCompName = chemCompName;
    }

    @JsonProperty("entity_id")
    public Integer getEntityId() {
        return entityId;
    }

    @JsonProperty("entity_id")
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @JsonProperty("residue_number")
    public Integer getResidueNumber() {
        return residueNumber;
    }

    @JsonProperty("residue_number")
    public void setResidueNumber(Integer residueNumber) {
        this.residueNumber = residueNumber;
    }

    @JsonProperty("author_residue_number")
    public Integer getAuthorResidueNumber() {
        return authorResidueNumber;
    }

    @JsonProperty("author_residue_number")
    public void setAuthorResidueNumber(Integer authorResidueNumber) {
        this.authorResidueNumber = authorResidueNumber;
    }

    @JsonProperty("chain_id")
    public String getChainId() {
        return chainId;
    }

    @JsonProperty("chain_id")
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    @JsonProperty("alternate_conformers")
    public Integer getAlternateConformers() {
        return alternateConformers;
    }

    @JsonProperty("alternate_conformers")
    public void setAlternateConformers(Integer alternateConformers) {
        this.alternateConformers = alternateConformers;
    }

    @JsonProperty("author_insertion_code")
    public String getAuthorInsertionCode() {
        return authorInsertionCode;
    }

    @JsonProperty("author_insertion_code")
    public void setAuthorInsertionCode(String authorInsertionCode) {
        this.authorInsertionCode = authorInsertionCode;
    }

    @JsonProperty("chem_comp_id")
    public String getChemCompId() {
        return chemCompId;
    }

    @JsonProperty("chem_comp_id")
    public void setChemCompId(String chemCompId) {
        this.chemCompId = chemCompId;
    }

    @JsonProperty("class")
    public String getClass_() {
        return _class;
    }

    @JsonProperty("class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    @JsonProperty("struct_asym_id")
    public String getStructAsymId() {
        return structAsymId;
    }

    @JsonProperty("struct_asym_id")
    public void setStructAsymId(String structAsymId) {
        this.structAsymId = structAsymId;
    }
}
