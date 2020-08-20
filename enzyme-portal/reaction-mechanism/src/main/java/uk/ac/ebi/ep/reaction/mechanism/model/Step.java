package uk.ac.ebi.ep.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "step_id",
    "description",
    "figure",
    "is_product"
})
/**
 *
 * @author Joseph
 */
@ToString
public class Step {

    @JsonProperty("step_id")
    private Integer stepId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("figure")
    private String figure;
    @JsonProperty("is_product")
    private Boolean isProduct;

    @JsonProperty("step_id")
    public Integer getStepId() {
        return stepId;
    }

    @JsonProperty("step_id")
    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("figure")
    public String getFigure() {
        return figure;
    }

    @JsonProperty("figure")
    public void setFigure(String figure) {
        this.figure = figure;
    }

    @JsonProperty("is_product")
    public Boolean getIsProduct() {
        return isProduct;
    }

    @JsonProperty("is_product")
    public void setIsProduct(Boolean isProduct) {
        this.isProduct = isProduct;
    }

}
