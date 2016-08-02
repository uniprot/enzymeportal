
package uk.ac.ebi.ep.centralservice.chembl.activity;

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
    "activity_comment",
    "activity_id",
    "assay_chembl_id",
    "assay_description",
    "assay_type",
    "bao_endpoint",
    "bao_format",
    "canonical_smiles",
    "data_validity_comment",
    "document_chembl_id",
    "document_journal",
    "document_year",
    "molecule_chembl_id",
    "pchembl_value",
    "potential_duplicate",
    "published_relation",
    "published_type",
    "published_units",
    "published_value",
    "qudt_units",
    "record_id",
    "standard_flag",
    "standard_relation",
    "standard_type",
    "standard_units",
    "standard_value",
    "target_chembl_id",
    "target_organism",
    "target_pref_name",
    "uo_units"
})
/**
 *
 * @author joseph
 */
public class Activity {

    @JsonProperty("activity_comment")
    private Object activityComment;
    @JsonProperty("activity_id")
    private Integer activityId;
    @JsonProperty("assay_chembl_id")
    private String assayChemblId;
    @JsonProperty("assay_description")
    private String assayDescription;
    @JsonProperty("assay_type")
    private String assayType;
    @JsonProperty("bao_endpoint")
    private String baoEndpoint;
    @JsonProperty("bao_format")
    private String baoFormat;
    @JsonProperty("canonical_smiles")
    private String canonicalSmiles;
    @JsonProperty("data_validity_comment")
    private Object dataValidityComment;
    @JsonProperty("document_chembl_id")
    private String documentChemblId;
    @JsonProperty("document_journal")
    private String documentJournal;
    @JsonProperty("document_year")
    private Integer documentYear;
    @JsonProperty("molecule_chembl_id")
    private String moleculeChemblId;
    @JsonProperty("pchembl_value")
    private String pchemblValue;
    @JsonProperty("potential_duplicate")
    private Object potentialDuplicate;
    @JsonProperty("published_relation")
    private String publishedRelation;
    @JsonProperty("published_type")
    private String publishedType;
    @JsonProperty("published_units")
    private String publishedUnits;
    @JsonProperty("published_value")
    private String publishedValue;
    @JsonProperty("qudt_units")
    private String qudtUnits;
    @JsonProperty("record_id")
    private Integer recordId;
    @JsonProperty("standard_flag")
    private Boolean standardFlag;
    @JsonProperty("standard_relation")
    private String standardRelation;
    @JsonProperty("standard_type")
    private String standardType;
    @JsonProperty("standard_units")
    private String standardUnits;
    @JsonProperty("standard_value")
    private String standardValue;
    @JsonProperty("target_chembl_id")
    private String targetChemblId;
    @JsonProperty("target_organism")
    private String targetOrganism;
    @JsonProperty("target_pref_name")
    private String targetPrefName;
    @JsonProperty("uo_units")
    private String uoUnits;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The activityComment
     */
    @JsonProperty("activity_comment")
    public Object getActivityComment() {
        return activityComment;
    }

    /**
     *
     * @param activityComment The activity_comment
     */
    @JsonProperty("activity_comment")
    public void setActivityComment(Object activityComment) {
        this.activityComment = activityComment;
    }

    /**
     *
     * @return The activityId
     */
    @JsonProperty("activity_id")
    public Integer getActivityId() {
        return activityId;
    }

    /**
     *
     * @param activityId The activity_id
     */
    @JsonProperty("activity_id")
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
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
     * @return The assayDescription
     */
    @JsonProperty("assay_description")
    public String getAssayDescription() {
        return assayDescription;
    }

    /**
     *
     * @param assayDescription The assay_description
     */
    @JsonProperty("assay_description")
    public void setAssayDescription(String assayDescription) {
        this.assayDescription = assayDescription;
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
     * @return The baoEndpoint
     */
    @JsonProperty("bao_endpoint")
    public String getBaoEndpoint() {
        return baoEndpoint;
    }

    /**
     *
     * @param baoEndpoint The bao_endpoint
     */
    @JsonProperty("bao_endpoint")
    public void setBaoEndpoint(String baoEndpoint) {
        this.baoEndpoint = baoEndpoint;
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
     * @return The dataValidityComment
     */
    @JsonProperty("data_validity_comment")
    public Object getDataValidityComment() {
        return dataValidityComment;
    }

    /**
     *
     * @param dataValidityComment The data_validity_comment
     */
    @JsonProperty("data_validity_comment")
    public void setDataValidityComment(Object dataValidityComment) {
        this.dataValidityComment = dataValidityComment;
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
     * @return The documentJournal
     */
    @JsonProperty("document_journal")
    public String getDocumentJournal() {
        return documentJournal;
    }

    /**
     *
     * @param documentJournal The document_journal
     */
    @JsonProperty("document_journal")
    public void setDocumentJournal(String documentJournal) {
        this.documentJournal = documentJournal;
    }

    /**
     *
     * @return The documentYear
     */
    @JsonProperty("document_year")
    public Integer getDocumentYear() {
        return documentYear;
    }

    /**
     *
     * @param documentYear The document_year
     */
    @JsonProperty("document_year")
    public void setDocumentYear(Integer documentYear) {
        this.documentYear = documentYear;
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
     * @return The pchemblValue
     */
    @JsonProperty("pchembl_value")
    public String getPchemblValue() {
        return pchemblValue;
    }

    /**
     *
     * @param pchemblValue The pchembl_value
     */
    @JsonProperty("pchembl_value")
    public void setPchemblValue(String pchemblValue) {
        this.pchemblValue = pchemblValue;
    }

    /**
     *
     * @return The potentialDuplicate
     */
    @JsonProperty("potential_duplicate")
    public Object getPotentialDuplicate() {
        return potentialDuplicate;
    }

    /**
     *
     * @param potentialDuplicate The potential_duplicate
     */
    @JsonProperty("potential_duplicate")
    public void setPotentialDuplicate(Object potentialDuplicate) {
        this.potentialDuplicate = potentialDuplicate;
    }

    /**
     *
     * @return The publishedRelation
     */
    @JsonProperty("published_relation")
    public String getPublishedRelation() {
        return publishedRelation;
    }

    /**
     *
     * @param publishedRelation The published_relation
     */
    @JsonProperty("published_relation")
    public void setPublishedRelation(String publishedRelation) {
        this.publishedRelation = publishedRelation;
    }

    /**
     *
     * @return The publishedType
     */
    @JsonProperty("published_type")
    public String getPublishedType() {
        return publishedType;
    }

    /**
     *
     * @param publishedType The published_type
     */
    @JsonProperty("published_type")
    public void setPublishedType(String publishedType) {
        this.publishedType = publishedType;
    }

    /**
     *
     * @return The publishedUnits
     */
    @JsonProperty("published_units")
    public String getPublishedUnits() {
        return publishedUnits;
    }

    /**
     *
     * @param publishedUnits The published_units
     */
    @JsonProperty("published_units")
    public void setPublishedUnits(String publishedUnits) {
        this.publishedUnits = publishedUnits;
    }

    /**
     *
     * @return The publishedValue
     */
    @JsonProperty("published_value")
    public String getPublishedValue() {
        return publishedValue;
    }

    /**
     *
     * @param publishedValue The published_value
     */
    @JsonProperty("published_value")
    public void setPublishedValue(String publishedValue) {
        this.publishedValue = publishedValue;
    }

    /**
     *
     * @return The qudtUnits
     */
    @JsonProperty("qudt_units")
    public String getQudtUnits() {
        return qudtUnits;
    }

    /**
     *
     * @param qudtUnits The qudt_units
     */
    @JsonProperty("qudt_units")
    public void setQudtUnits(String qudtUnits) {
        this.qudtUnits = qudtUnits;
    }

    /**
     *
     * @return The recordId
     */
    @JsonProperty("record_id")
    public Integer getRecordId() {
        return recordId;
    }

    /**
     *
     * @param recordId The record_id
     */
    @JsonProperty("record_id")
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     *
     * @return The standardFlag
     */
    @JsonProperty("standard_flag")
    public Boolean getStandardFlag() {
        return standardFlag;
    }

    /**
     *
     * @param standardFlag The standard_flag
     */
    @JsonProperty("standard_flag")
    public void setStandardFlag(Boolean standardFlag) {
        this.standardFlag = standardFlag;
    }

    /**
     *
     * @return The standardRelation
     */
    @JsonProperty("standard_relation")
    public String getStandardRelation() {
        return standardRelation;
    }

    /**
     *
     * @param standardRelation The standard_relation
     */
    @JsonProperty("standard_relation")
    public void setStandardRelation(String standardRelation) {
        this.standardRelation = standardRelation;
    }

    /**
     *
     * @return The standardType
     */
    @JsonProperty("standard_type")
    public String getStandardType() {
        return standardType;
    }

    /**
     *
     * @param standardType The standard_type
     */
    @JsonProperty("standard_type")
    public void setStandardType(String standardType) {
        this.standardType = standardType;
    }

    /**
     *
     * @return The standardUnits
     */
    @JsonProperty("standard_units")
    public String getStandardUnits() {
        return standardUnits;
    }

    /**
     *
     * @param standardUnits The standard_units
     */
    @JsonProperty("standard_units")
    public void setStandardUnits(String standardUnits) {
        this.standardUnits = standardUnits;
    }

    /**
     *
     * @return The standardValue
     */
    @JsonProperty("standard_value")
    public String getStandardValue() {
        return standardValue;
    }

    /**
     *
     * @param standardValue The standard_value
     */
    @JsonProperty("standard_value")
    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
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

    /**
     *
     * @return The targetOrganism
     */
    @JsonProperty("target_organism")
    public String getTargetOrganism() {
        return targetOrganism;
    }

    /**
     *
     * @param targetOrganism The target_organism
     */
    @JsonProperty("target_organism")
    public void setTargetOrganism(String targetOrganism) {
        this.targetOrganism = targetOrganism;
    }

    /**
     *
     * @return The targetPrefName
     */
    @JsonProperty("target_pref_name")
    public String getTargetPrefName() {
        return targetPrefName;
    }

    /**
     *
     * @param targetPrefName The target_pref_name
     */
    @JsonProperty("target_pref_name")
    public void setTargetPrefName(String targetPrefName) {
        this.targetPrefName = targetPrefName;
    }

    /**
     *
     * @return The uoUnits
     */
    @JsonProperty("uo_units")
    public String getUoUnits() {
        return uoUnits;
    }

    /**
     *
     * @param uoUnits The uo_units
     */
    @JsonProperty("uo_units")
    public void setUoUnits(String uoUnits) {
        this.uoUnits = uoUnits;
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
        hash = 37 * hash + Objects.hashCode(this.moleculeChemblId);
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
        final Activity other = (Activity) obj;
        if (!Objects.equals(this.moleculeChemblId, other.moleculeChemblId)) {
            return false;
        }
        return true;
    }
    
    
}
