package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mechanism_id",
    "is_detailed",
    "mechanism_text",
    "rating",
    "components_summary",
    "steps"
})
/**
 *
 * @author Joseph
 */
@ToString
public class Mechanism {

    @JsonProperty("mechanism_id")
    private Integer mechanismId;
    @JsonProperty("is_detailed")
    private Boolean isDetailed;
    @JsonProperty("mechanism_text")
    private String mechanismText;
    @JsonProperty("rating")
    private Integer rating;
    @JsonProperty("components_summary")
    private String componentsSummary;
    @JsonProperty("steps")
    private List<Step> steps = new ArrayList<>();

    @JsonProperty("mechanism_id")
    public Integer getMechanismId() {
        return mechanismId;
    }

    @JsonProperty("mechanism_id")
    public void setMechanismId(Integer mechanismId) {
        this.mechanismId = mechanismId;
    }

    @JsonProperty("is_detailed")
    public Boolean getIsDetailed() {
        return isDetailed;
    }

    @JsonProperty("is_detailed")
    public void setIsDetailed(Boolean isDetailed) {
        this.isDetailed = isDetailed;
    }

    @JsonProperty("mechanism_text")
    public String getMechanismText() {
        return mechanismText;
    }

    @JsonProperty("mechanism_text")
    public void setMechanismText(String mechanismText) {
        this.mechanismText = mechanismText;
    }

    @JsonProperty("rating")
    public Integer getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonProperty("components_summary")
    public String getComponentsSummary() {
        return componentsSummary;
    }

    @JsonProperty("components_summary")
    public void setComponentsSummary(String componentsSummary) {
        this.componentsSummary = componentsSummary;
    }

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
