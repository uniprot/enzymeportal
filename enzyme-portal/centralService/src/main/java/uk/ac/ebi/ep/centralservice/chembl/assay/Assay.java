package uk.ac.ebi.ep.centralservice.chembl.assay;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "assay_category",
    "assay_cell_type",
    "assay_chembl_id",
    "assay_organism",
    "assay_strain",
    "assay_subcellular_fraction",
    "assay_tax_id",
    "assay_test_type",
    "assay_tissue",
    "assay_type",
    "assay_type_description",
    "bao_format",
    "cell_chembl_id",
    "confidence_description",
    "confidence_score",
    "description",
    "document_chembl_id",
    "relationship_description",
    "relationship_type",
    "src_assay_id",
    "src_id",
    "target_chembl_id"
})
/**
 *
 * @author joseph
 */
public class Assay {

    @JsonProperty("assay_category")
    private Object assayCategory;
    @JsonProperty("assay_cell_type")
    private Object assayCellType;
    @JsonProperty("assay_chembl_id")
    private String assayChemblId;
    @JsonProperty("assay_organism")
    private String assayOrganism;
    @JsonProperty("assay_strain")
    private Object assayStrain;
    @JsonProperty("assay_subcellular_fraction")
    private Object assaySubcellularFraction;
    @JsonProperty("assay_tax_id")
    private Integer assayTaxId;
    @JsonProperty("assay_test_type")
    private Object assayTestType;
    @JsonProperty("assay_tissue")
    private Object assayTissue;
    @JsonProperty("assay_type")
    private String assayType;
    @JsonProperty("assay_type_description")
    private String assayTypeDescription;
    @JsonProperty("bao_format")
    private String baoFormat;
    @JsonProperty("cell_chembl_id")
    private Object cellChemblId;
    @JsonProperty("confidence_description")
    private String confidenceDescription;
    @JsonProperty("confidence_score")
    private Integer confidenceScore;
    @JsonProperty("description")
    private String description;
    @JsonProperty("document_chembl_id")
    private String documentChemblId;
    @JsonProperty("relationship_description")
    private String relationshipDescription;
    @JsonProperty("relationship_type")
    private String relationshipType;
    @JsonProperty("src_assay_id")
    private String srcAssayId;
    @JsonProperty("src_id")
    private Integer srcId;
    @JsonProperty("target_chembl_id")
    private String targetChemblId;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The assayCategory
     */
    @JsonProperty("assay_category")
    public Object getAssayCategory() {
        return assayCategory;
    }

    /**
     *
     * @param assayCategory The assay_category
     */
    @JsonProperty("assay_category")
    public void setAssayCategory(Object assayCategory) {
        this.assayCategory = assayCategory;
    }

    /**
     *
     * @return The assayCellType
     */
    @JsonProperty("assay_cell_type")
    public Object getAssayCellType() {
        return assayCellType;
    }

    /**
     *
     * @param assayCellType The assay_cell_type
     */
    @JsonProperty("assay_cell_type")
    public void setAssayCellType(Object assayCellType) {
        this.assayCellType = assayCellType;
    }

    /**
     *
     * @return The assayChemblId
     */
    @JsonProperty("assay_chembl_id")
    public String getAssayChemblId() {
        return assayChemblId;
    }

    /**
     *
     * @param assayChemblId The assay_chembl_id
     */
    @JsonProperty("assay_chembl_id")
    public void setAssayChemblId(String assayChemblId) {
        this.assayChemblId = assayChemblId;
    }

    /**
     *
     * @return The assayOrganism
     */
    @JsonProperty("assay_organism")
    public String getAssayOrganism() {
        return assayOrganism;
    }

    /**
     *
     * @param assayOrganism The assay_organism
     */
    @JsonProperty("assay_organism")
    public void setAssayOrganism(String assayOrganism) {
        this.assayOrganism = assayOrganism;
    }

    /**
     *
     * @return The assayStrain
     */
    @JsonProperty("assay_strain")
    public Object getAssayStrain() {
        return assayStrain;
    }

    /**
     *
     * @param assayStrain The assay_strain
     */
    @JsonProperty("assay_strain")
    public void setAssayStrain(Object assayStrain) {
        this.assayStrain = assayStrain;
    }

    /**
     *
     * @return The assaySubcellularFraction
     */
    @JsonProperty("assay_subcellular_fraction")
    public Object getAssaySubcellularFraction() {
        return assaySubcellularFraction;
    }

    /**
     *
     * @param assaySubcellularFraction The assay_subcellular_fraction
     */
    @JsonProperty("assay_subcellular_fraction")
    public void setAssaySubcellularFraction(Object assaySubcellularFraction) {
        this.assaySubcellularFraction = assaySubcellularFraction;
    }

    /**
     *
     * @return The assayTaxId
     */
    @JsonProperty("assay_tax_id")
    public Integer getAssayTaxId() {
        return assayTaxId;
    }

    /**
     *
     * @param assayTaxId The assay_tax_id
     */
    @JsonProperty("assay_tax_id")
    public void setAssayTaxId(Integer assayTaxId) {
        this.assayTaxId = assayTaxId;
    }

    /**
     *
     * @return The assayTestType
     */
    @JsonProperty("assay_test_type")
    public Object getAssayTestType() {
        return assayTestType;
    }

    /**
     *
     * @param assayTestType The assay_test_type
     */
    @JsonProperty("assay_test_type")
    public void setAssayTestType(Object assayTestType) {
        this.assayTestType = assayTestType;
    }

    /**
     *
     * @return The assayTissue
     */
    @JsonProperty("assay_tissue")
    public Object getAssayTissue() {
        return assayTissue;
    }

    /**
     *
     * @param assayTissue The assay_tissue
     */
    @JsonProperty("assay_tissue")
    public void setAssayTissue(Object assayTissue) {
        this.assayTissue = assayTissue;
    }

    /**
     *
     * @return The assayType
     */
    @JsonProperty("assay_type")
    public String getAssayType() {
        return assayType;
    }

    /**
     *
     * @param assayType The assay_type
     */
    @JsonProperty("assay_type")
    public void setAssayType(String assayType) {
        this.assayType = assayType;
    }

    /**
     *
     * @return The assayTypeDescription
     */
    @JsonProperty("assay_type_description")
    public String getAssayTypeDescription() {
        return assayTypeDescription;
    }

    /**
     *
     * @param assayTypeDescription The assay_type_description
     */
    @JsonProperty("assay_type_description")
    public void setAssayTypeDescription(String assayTypeDescription) {
        this.assayTypeDescription = assayTypeDescription;
    }

    /**
     *
     * @return The baoFormat
     */
    @JsonProperty("bao_format")
    public String getBaoFormat() {
        return baoFormat;
    }

    /**
     *
     * @param baoFormat The bao_format
     */
    @JsonProperty("bao_format")
    public void setBaoFormat(String baoFormat) {
        this.baoFormat = baoFormat;
    }

    /**
     *
     * @return The cellChemblId
     */
    @JsonProperty("cell_chembl_id")
    public Object getCellChemblId() {
        return cellChemblId;
    }

    /**
     *
     * @param cellChemblId The cell_chembl_id
     */
    @JsonProperty("cell_chembl_id")
    public void setCellChemblId(Object cellChemblId) {
        this.cellChemblId = cellChemblId;
    }

    /**
     *
     * @return The confidenceDescription
     */
    @JsonProperty("confidence_description")
    public String getConfidenceDescription() {
        return confidenceDescription;
    }

    /**
     *
     * @param confidenceDescription The confidence_description
     */
    @JsonProperty("confidence_description")
    public void setConfidenceDescription(String confidenceDescription) {
        this.confidenceDescription = confidenceDescription;
    }

    /**
     *
     * @return The confidenceScore
     */
    @JsonProperty("confidence_score")
    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    /**
     *
     * @param confidenceScore The confidence_score
     */
    @JsonProperty("confidence_score")
    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    /**
     *
     * @return The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return The documentChemblId
     */
    @JsonProperty("document_chembl_id")
    public String getDocumentChemblId() {
        return documentChemblId;
    }

    /**
     *
     * @param documentChemblId The document_chembl_id
     */
    @JsonProperty("document_chembl_id")
    public void setDocumentChemblId(String documentChemblId) {
        this.documentChemblId = documentChemblId;
    }

    /**
     *
     * @return The relationshipDescription
     */
    @JsonProperty("relationship_description")
    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    /**
     *
     * @param relationshipDescription The relationship_description
     */
    @JsonProperty("relationship_description")
    public void setRelationshipDescription(String relationshipDescription) {
        this.relationshipDescription = relationshipDescription;
    }

    /**
     *
     * @return The relationshipType
     */
    @JsonProperty("relationship_type")
    public String getRelationshipType() {
        return relationshipType;
    }

    /**
     *
     * @param relationshipType The relationship_type
     */
    @JsonProperty("relationship_type")
    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     *
     * @return The srcAssayId
     */
    @JsonProperty("src_assay_id")
    public String getSrcAssayId() {
        return srcAssayId;
    }

    /**
     *
     * @param srcAssayId The src_assay_id
     */
    @JsonProperty("src_assay_id")
    public void setSrcAssayId(String srcAssayId) {
        this.srcAssayId = srcAssayId;
    }

    /**
     *
     * @return The srcId
     */
    @JsonProperty("src_id")
    public Integer getSrcId() {
        return srcId;
    }

    /**
     *
     * @param srcId The src_id
     */
    @JsonProperty("src_id")
    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    /**
     *
     * @return The targetChemblId
     */
    @JsonProperty("target_chembl_id")
    public String getTargetChemblId() {
        return targetChemblId;
    }

    /**
     *
     * @param targetChemblId The target_chembl_id
     */
    @JsonProperty("target_chembl_id")
    public void setTargetChemblId(String targetChemblId) {
        this.targetChemblId = targetChemblId;
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
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.assayChemblId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Assay other = (Assay) obj;
        if (!Objects.equals(this.assayChemblId, other.assayChemblId)) {
            return false;
        }
        return true;
    }

}
