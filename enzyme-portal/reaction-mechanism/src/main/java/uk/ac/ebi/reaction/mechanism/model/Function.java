package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "function_type",
    "function",
    "emo",
    "group_function"
})
/**
 *
 * @author Joseph
 */
public class Function {

    @JsonProperty("function_type")
    private String functionType;
    @JsonProperty("function")
    private String functionFunction;
    @JsonProperty("emo")
    private String emo;
    @JsonProperty("group_function")
    private String groupFunction;

    @JsonProperty("function_type")
    public String getFunctionType() {
        return functionType;
    }

    @JsonProperty("function_type")
    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    @JsonProperty("function")
    public String getFunctionFunction() {
        return functionFunction;
    }

    @JsonProperty("function")
    public void setFunctionFunction(String functionFunction) {
        this.functionFunction = functionFunction;
    }

    @JsonProperty("emo")
    public String getEmo() {
        return emo;
    }

    @JsonProperty("emo")
    public void setEmo(String emo) {
        this.emo = emo;
    }

    @JsonProperty("group_function")
    public String getGroupFunction() {
        return groupFunction;
    }

    @JsonProperty("group_function")
    public void setGroupFunction(String groupFunction) {
        this.groupFunction = groupFunction;
    }
}
