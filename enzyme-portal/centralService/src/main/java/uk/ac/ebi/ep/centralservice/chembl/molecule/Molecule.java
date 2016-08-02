package uk.ac.ebi.ep.centralservice.chembl.molecule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "atc_classifications",
    "availability_type",
    "biotherapeutic",
    "black_box_warning",
    "chebi_par_id",
    "chirality",
    "dosed_ingredient",
    "first_approval",
    "first_in_class",
    "helm_notation",
    "indication_class",
    "inorganic_flag",
    "max_phase",
    "molecule_chembl_id",
    "molecule_hierarchy",
    "molecule_properties",
    "molecule_structures",
    "molecule_synonyms",
    "molecule_type",
    "natural_product",
    "oral",
    "parenteral",
    "polymer_flag",
    "pref_name",
    "prodrug",
    "structure_type",
    "therapeutic_flag",
    "topical",
    "usan_stem",
    "usan_stem_definition",
    "usan_substem",
    "usan_year"
})
/**
 *
 * @author joseph
 */
public class Molecule {

    @JsonProperty("atc_classifications")
    private List<Object> atcClassifications = new ArrayList<Object>();
    @JsonProperty("availability_type")
    private String availabilityType;
    @JsonProperty("biotherapeutic")
    private Object biotherapeutic;
    @JsonProperty("black_box_warning")
    private String blackBoxWarning;
    @JsonProperty("chebi_par_id")
    private Object chebiParId;
    @JsonProperty("chirality")
    private String chirality;
    @JsonProperty("dosed_ingredient")
    private Boolean dosedIngredient;
    @JsonProperty("first_approval")
    private Integer firstApproval;
    @JsonProperty("first_in_class")
    private String firstInClass;
    @JsonProperty("helm_notation")
    private Object helmNotation;
    @JsonProperty("indication_class")
    private Object indicationClass;
    @JsonProperty("inorganic_flag")
    private String inorganicFlag;
    @JsonProperty("max_phase")
    private Integer maxPhase;
    @JsonProperty("molecule_chembl_id")
    private String moleculeChemblId;
    @JsonProperty("molecule_hierarchy")
    private MoleculeHierarchy moleculeHierarchy;
    @JsonProperty("molecule_properties")
    private MoleculeProperties moleculeProperties;
    @JsonProperty("molecule_structures")
    private MoleculeStructures moleculeStructures;
    @JsonProperty("molecule_synonyms")
    private List<MoleculeSynonym> moleculeSynonyms = new ArrayList<MoleculeSynonym>();
    @JsonProperty("molecule_type")
    private String moleculeType;
    @JsonProperty("natural_product")
    private String naturalProduct;
    @JsonProperty("oral")
    private Boolean oral;
    @JsonProperty("parenteral")
    private Boolean parenteral;
    @JsonProperty("polymer_flag")
    private Boolean polymerFlag;
    @JsonProperty("pref_name")
    private String prefName;
    @JsonProperty("prodrug")
    private String prodrug;
    @JsonProperty("structure_type")
    private String structureType;
    @JsonProperty("therapeutic_flag")
    private Boolean therapeuticFlag;
    @JsonProperty("topical")
    private Boolean topical;
    @JsonProperty("usan_stem")
    private String usanStem;
    @JsonProperty("usan_stem_definition")
    private String usanStemDefinition;
    @JsonProperty("usan_substem")
    private Object usanSubstem;
    @JsonProperty("usan_year")
    private Object usanYear;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The atcClassifications
     */
    @JsonProperty("atc_classifications")
    public List<Object> getAtcClassifications() {
        return atcClassifications;
    }

    /**
     *
     * @param atcClassifications The atc_classifications
     */
    @JsonProperty("atc_classifications")
    public void setAtcClassifications(List<Object> atcClassifications) {
        this.atcClassifications = atcClassifications;
    }

    /**
     *
     * @return The availabilityType
     */
    @JsonProperty("availability_type")
    public String getAvailabilityType() {
        return availabilityType;
    }

    /**
     *
     * @param availabilityType The availability_type
     */
    @JsonProperty("availability_type")
    public void setAvailabilityType(String availabilityType) {
        this.availabilityType = availabilityType;
    }

    /**
     *
     * @return The biotherapeutic
     */
    @JsonProperty("biotherapeutic")
    public Object getBiotherapeutic() {
        return biotherapeutic;
    }

    /**
     *
     * @param biotherapeutic The biotherapeutic
     */
    @JsonProperty("biotherapeutic")
    public void setBiotherapeutic(Object biotherapeutic) {
        this.biotherapeutic = biotherapeutic;
    }

    /**
     *
     * @return The blackBoxWarning
     */
    @JsonProperty("black_box_warning")
    public String getBlackBoxWarning() {
        return blackBoxWarning;
    }

    /**
     *
     * @param blackBoxWarning The black_box_warning
     */
    @JsonProperty("black_box_warning")
    public void setBlackBoxWarning(String blackBoxWarning) {
        this.blackBoxWarning = blackBoxWarning;
    }

    /**
     *
     * @return The chebiParId
     */
    @JsonProperty("chebi_par_id")
    public Object getChebiParId() {
        return chebiParId;
    }

    /**
     *
     * @param chebiParId The chebi_par_id
     */
    @JsonProperty("chebi_par_id")
    public void setChebiParId(Object chebiParId) {
        this.chebiParId = chebiParId;
    }

    /**
     *
     * @return The chirality
     */
    @JsonProperty("chirality")
    public String getChirality() {
        return chirality;
    }

    /**
     *
     * @param chirality The chirality
     */
    @JsonProperty("chirality")
    public void setChirality(String chirality) {
        this.chirality = chirality;
    }

    /**
     *
     * @return The dosedIngredient
     */
    @JsonProperty("dosed_ingredient")
    public Boolean getDosedIngredient() {
        return dosedIngredient;
    }

    /**
     *
     * @param dosedIngredient The dosed_ingredient
     */
    @JsonProperty("dosed_ingredient")
    public void setDosedIngredient(Boolean dosedIngredient) {
        this.dosedIngredient = dosedIngredient;
    }

    /**
     *
     * @return The firstApproval
     */
    @JsonProperty("first_approval")
    public Integer getFirstApproval() {
        return firstApproval;
    }

    /**
     *
     * @param firstApproval The first_approval
     */
    @JsonProperty("first_approval")
    public void setFirstApproval(Integer firstApproval) {
        this.firstApproval = firstApproval;
    }

    /**
     *
     * @return The firstInClass
     */
    @JsonProperty("first_in_class")
    public String getFirstInClass() {
        return firstInClass;
    }

    /**
     *
     * @param firstInClass The first_in_class
     */
    @JsonProperty("first_in_class")
    public void setFirstInClass(String firstInClass) {
        this.firstInClass = firstInClass;
    }

    /**
     *
     * @return The helmNotation
     */
    @JsonProperty("helm_notation")
    public Object getHelmNotation() {
        return helmNotation;
    }

    /**
     *
     * @param helmNotation The helm_notation
     */
    @JsonProperty("helm_notation")
    public void setHelmNotation(Object helmNotation) {
        this.helmNotation = helmNotation;
    }

    /**
     *
     * @return The indicationClass
     */
    @JsonProperty("indication_class")
    public Object getIndicationClass() {
        return indicationClass;
    }

    /**
     *
     * @param indicationClass The indication_class
     */
    @JsonProperty("indication_class")
    public void setIndicationClass(Object indicationClass) {
        this.indicationClass = indicationClass;
    }

    /**
     *
     * @return The inorganicFlag
     */
    @JsonProperty("inorganic_flag")
    public String getInorganicFlag() {
        return inorganicFlag;
    }

    /**
     *
     * @param inorganicFlag The inorganic_flag
     */
    @JsonProperty("inorganic_flag")
    public void setInorganicFlag(String inorganicFlag) {
        this.inorganicFlag = inorganicFlag;
    }

    /**
     *
     * @return The maxPhase
     */
    @JsonProperty("max_phase")
    public Integer getMaxPhase() {
        return maxPhase;
    }

    /**
     *
     * @param maxPhase The max_phase
     */
    @JsonProperty("max_phase")
    public void setMaxPhase(Integer maxPhase) {
        this.maxPhase = maxPhase;
    }

    /**
     *
     * @return The moleculeChemblId
     */
    @JsonProperty("molecule_chembl_id")
    public String getMoleculeChemblId() {
        return moleculeChemblId;
    }

    /**
     *
     * @param moleculeChemblId The molecule_chembl_id
     */
    @JsonProperty("molecule_chembl_id")
    public void setMoleculeChemblId(String moleculeChemblId) {
        this.moleculeChemblId = moleculeChemblId;
    }

    /**
     *
     * @return The moleculeHierarchy
     */
    @JsonProperty("molecule_hierarchy")
    public MoleculeHierarchy getMoleculeHierarchy() {
        return moleculeHierarchy;
    }

    /**
     *
     * @param moleculeHierarchy The molecule_hierarchy
     */
    @JsonProperty("molecule_hierarchy")
    public void setMoleculeHierarchy(MoleculeHierarchy moleculeHierarchy) {
        this.moleculeHierarchy = moleculeHierarchy;
    }

    /**
     *
     * @return The moleculeProperties
     */
    @JsonProperty("molecule_properties")
    public MoleculeProperties getMoleculeProperties() {
        return moleculeProperties;
    }

    /**
     *
     * @param moleculeProperties The molecule_properties
     */
    @JsonProperty("molecule_properties")
    public void setMoleculeProperties(MoleculeProperties moleculeProperties) {
        this.moleculeProperties = moleculeProperties;
    }

    /**
     *
     * @return The moleculeStructures
     */
    @JsonProperty("molecule_structures")
    public MoleculeStructures getMoleculeStructures() {
        return moleculeStructures;
    }

    /**
     *
     * @param moleculeStructures The molecule_structures
     */
    @JsonProperty("molecule_structures")
    public void setMoleculeStructures(MoleculeStructures moleculeStructures) {
        this.moleculeStructures = moleculeStructures;
    }

    /**
     *
     * @return The moleculeSynonyms
     */
    @JsonProperty("molecule_synonyms")
    public List<MoleculeSynonym> getMoleculeSynonyms() {
        return moleculeSynonyms;
    }

    /**
     *
     * @param moleculeSynonyms The molecule_synonyms
     */
    @JsonProperty("molecule_synonyms")
    public void setMoleculeSynonyms(List<MoleculeSynonym> moleculeSynonyms) {
        this.moleculeSynonyms = moleculeSynonyms;
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
     * @return The naturalProduct
     */
    @JsonProperty("natural_product")
    public String getNaturalProduct() {
        return naturalProduct;
    }

    /**
     *
     * @param naturalProduct The natural_product
     */
    @JsonProperty("natural_product")
    public void setNaturalProduct(String naturalProduct) {
        this.naturalProduct = naturalProduct;
    }

    /**
     *
     * @return The oral
     */
    @JsonProperty("oral")
    public Boolean getOral() {
        return oral;
    }

    /**
     *
     * @param oral The oral
     */
    @JsonProperty("oral")
    public void setOral(Boolean oral) {
        this.oral = oral;
    }

    /**
     *
     * @return The parenteral
     */
    @JsonProperty("parenteral")
    public Boolean getParenteral() {
        return parenteral;
    }

    /**
     *
     * @param parenteral The parenteral
     */
    @JsonProperty("parenteral")
    public void setParenteral(Boolean parenteral) {
        this.parenteral = parenteral;
    }

    /**
     *
     * @return The polymerFlag
     */
    @JsonProperty("polymer_flag")
    public Boolean getPolymerFlag() {
        return polymerFlag;
    }

    /**
     *
     * @param polymerFlag The polymer_flag
     */
    @JsonProperty("polymer_flag")
    public void setPolymerFlag(Boolean polymerFlag) {
        this.polymerFlag = polymerFlag;
    }

    /**
     *
     * @return The prefName
     */
    @JsonProperty("pref_name")
    public String getPrefName() {
        return prefName;
    }

    /**
     *
     * @param prefName The pref_name
     */
    @JsonProperty("pref_name")
    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    /**
     *
     * @return The prodrug
     */
    @JsonProperty("prodrug")
    public String getProdrug() {
        return prodrug;
    }

    /**
     *
     * @param prodrug The prodrug
     */
    @JsonProperty("prodrug")
    public void setProdrug(String prodrug) {
        this.prodrug = prodrug;
    }

    /**
     *
     * @return The structureType
     */
    @JsonProperty("structure_type")
    public String getStructureType() {
        return structureType;
    }

    /**
     *
     * @param structureType The structure_type
     */
    @JsonProperty("structure_type")
    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    /**
     *
     * @return The therapeuticFlag
     */
    @JsonProperty("therapeutic_flag")
    public Boolean getTherapeuticFlag() {
        return therapeuticFlag;
    }

    /**
     *
     * @param therapeuticFlag The therapeutic_flag
     */
    @JsonProperty("therapeutic_flag")
    public void setTherapeuticFlag(Boolean therapeuticFlag) {
        this.therapeuticFlag = therapeuticFlag;
    }

    /**
     *
     * @return The topical
     */
    @JsonProperty("topical")
    public Boolean getTopical() {
        return topical;
    }

    /**
     *
     * @param topical The topical
     */
    @JsonProperty("topical")
    public void setTopical(Boolean topical) {
        this.topical = topical;
    }

    /**
     *
     * @return The usanStem
     */
    @JsonProperty("usan_stem")
    public String getUsanStem() {
        return usanStem;
    }

    /**
     *
     * @param usanStem The usan_stem
     */
    @JsonProperty("usan_stem")
    public void setUsanStem(String usanStem) {
        this.usanStem = usanStem;
    }

    /**
     *
     * @return The usanStemDefinition
     */
    @JsonProperty("usan_stem_definition")
    public String getUsanStemDefinition() {
        return usanStemDefinition;
    }

    /**
     *
     * @param usanStemDefinition The usan_stem_definition
     */
    @JsonProperty("usan_stem_definition")
    public void setUsanStemDefinition(String usanStemDefinition) {
        this.usanStemDefinition = usanStemDefinition;
    }

    /**
     *
     * @return The usanSubstem
     */
    @JsonProperty("usan_substem")
    public Object getUsanSubstem() {
        return usanSubstem;
    }

    /**
     *
     * @param usanSubstem The usan_substem
     */
    @JsonProperty("usan_substem")
    public void setUsanSubstem(Object usanSubstem) {
        this.usanSubstem = usanSubstem;
    }

    /**
     *
     * @return The usanYear
     */
    @JsonProperty("usan_year")
    public Object getUsanYear() {
        return usanYear;
    }

    /**
     *
     * @param usanYear The usan_year
     */
    @JsonProperty("usan_year")
    public void setUsanYear(Object usanYear) {
        this.usanYear = usanYear;
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
        return "Molecule{" + "moleculeChemblId=" + moleculeChemblId + ", moleculeType=" + moleculeType + ", prefName=" + prefName + '}';
    }

}
