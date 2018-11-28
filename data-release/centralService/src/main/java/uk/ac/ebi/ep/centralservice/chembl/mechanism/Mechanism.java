package uk.ac.ebi.ep.centralservice.chembl.mechanism;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "action_type",
    "binding_site_comment",
    "direct_interaction",
    "disease_efficacy",
    "max_phase",
    "mec_id",
    "mechanism_comment",
    "mechanism_of_action",
    "mechanism_refs",
    "molecular_mechanism",
    "molecule_chembl_id",
    "record_id",
    "selectivity_comment",
    "site_id",
    "target_chembl_id"
})
/**
 *
 * @author joseph
 */
public class Mechanism {

    @JsonProperty("action_type")
    private String actionType;
    @JsonProperty("binding_site_comment")
    private Object bindingSiteComment;
    @JsonProperty("direct_interaction")
    private Boolean directInteraction;
    @JsonProperty("disease_efficacy")
    private Boolean diseaseEfficacy;
    @JsonProperty("max_phase")
    private Integer maxPhase;
    @JsonProperty("mec_id")
    private Integer mecId;
    @JsonProperty("mechanism_comment")
    private Object mechanismComment;
    @JsonProperty("mechanism_of_action")
    private String mechanismOfAction;
    @JsonProperty("mechanism_refs")
    private List<MechanismRef> mechanismRefs = new ArrayList<MechanismRef>();
    @JsonProperty("molecular_mechanism")
    private Boolean molecularMechanism;
    @JsonProperty("molecule_chembl_id")
    private String moleculeChemblId;
    @JsonProperty("record_id")
    private Integer recordId;
    @JsonProperty("selectivity_comment")
    private Object selectivityComment;
    @JsonProperty("site_id")
    private Object siteId;
    @JsonProperty("target_chembl_id")
    private String targetChemblId;

    @JsonProperty("action_type")
    public String getActionType() {
        return actionType;
    }

    @JsonProperty("action_type")
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @JsonProperty("binding_site_comment")
    public Object getBindingSiteComment() {
        return bindingSiteComment;
    }

    @JsonProperty("binding_site_comment")
    public void setBindingSiteComment(Object bindingSiteComment) {
        this.bindingSiteComment = bindingSiteComment;
    }

    @JsonProperty("direct_interaction")
    public Boolean getDirectInteraction() {
        return directInteraction;
    }

    @JsonProperty("direct_interaction")
    public void setDirectInteraction(Boolean directInteraction) {
        this.directInteraction = directInteraction;
    }

    @JsonProperty("disease_efficacy")
    public Boolean getDiseaseEfficacy() {
        return diseaseEfficacy;
    }

    @JsonProperty("disease_efficacy")
    public void setDiseaseEfficacy(Boolean diseaseEfficacy) {
        this.diseaseEfficacy = diseaseEfficacy;
    }

    @JsonProperty("max_phase")
    public Integer getMaxPhase() {
        return maxPhase;
    }

    @JsonProperty("max_phase")
    public void setMaxPhase(Integer maxPhase) {
        this.maxPhase = maxPhase;
    }

    @JsonProperty("mec_id")
    public Integer getMecId() {
        return mecId;
    }

    @JsonProperty("mec_id")
    public void setMecId(Integer mecId) {
        this.mecId = mecId;
    }

    @JsonProperty("mechanism_comment")
    public Object getMechanismComment() {
        return mechanismComment;
    }

    @JsonProperty("mechanism_comment")
    public void setMechanismComment(Object mechanismComment) {
        this.mechanismComment = mechanismComment;
    }

    @JsonProperty("mechanism_of_action")
    public String getMechanismOfAction() {
        return mechanismOfAction;
    }

    @JsonProperty("mechanism_of_action")
    public void setMechanismOfAction(String mechanismOfAction) {
        this.mechanismOfAction = mechanismOfAction;
    }

    @JsonProperty("mechanism_refs")
    public List<MechanismRef> getMechanismRefs() {
        return mechanismRefs;
    }

    @JsonProperty("mechanism_refs")
    public void setMechanismRefs(List<MechanismRef> mechanismRefs) {
        this.mechanismRefs = mechanismRefs;
    }

    @JsonProperty("molecular_mechanism")
    public Boolean getMolecularMechanism() {
        return molecularMechanism;
    }

    @JsonProperty("molecular_mechanism")
    public void setMolecularMechanism(Boolean molecularMechanism) {
        this.molecularMechanism = molecularMechanism;
    }

    @JsonProperty("molecule_chembl_id")
    public String getMoleculeChemblId() {
        return moleculeChemblId;
    }

    @JsonProperty("molecule_chembl_id")
    public void setMoleculeChemblId(String moleculeChemblId) {
        this.moleculeChemblId = moleculeChemblId;
    }

    @JsonProperty("record_id")
    public Integer getRecordId() {
        return recordId;
    }

    @JsonProperty("record_id")
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    @JsonProperty("selectivity_comment")
    public Object getSelectivityComment() {
        return selectivityComment;
    }

    @JsonProperty("selectivity_comment")
    public void setSelectivityComment(Object selectivityComment) {
        this.selectivityComment = selectivityComment;
    }

    @JsonProperty("site_id")
    public Object getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(Object siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("target_chembl_id")
    public String getTargetChemblId() {
        return targetChemblId;
    }

    @JsonProperty("target_chembl_id")
    public void setTargetChemblId(String targetChemblId) {
        this.targetChemblId = targetChemblId;
    }

}
