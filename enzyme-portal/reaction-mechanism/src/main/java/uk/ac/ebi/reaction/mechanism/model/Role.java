package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "group_function",
    "function_type",
    "function",
    "emo"
})
/**
 *
 * @author Joseph
 */
public class Role {

    @JsonProperty("group_function")
    private String groupFunction;
    @JsonProperty("function_type")
    private String functionType;
    @JsonProperty("function")
    private String function;
    @JsonProperty("emo")
    private String emo;

    @JsonProperty("group_function")
    public String getGroupFunction() {
        return groupFunction;
    }

    @JsonProperty("group_function")
    public void setGroupFunction(String groupFunction) {
        this.groupFunction = groupFunction;
    }

    @JsonProperty("function_type")
    public String getFunctionType() {
        return functionType;
    }

    @JsonProperty("function_type")
    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    @JsonProperty("function")
    public String getFunction() {
        return function;
    }

    @JsonProperty("function")
    public void setFunction(String function) {
        this.function = function;
    }

    @JsonProperty("emo")
    public String getEmo() {
        return emo;
    }

    @JsonProperty("emo")
    public void setEmo(String emo) {
        this.emo = emo;
    }

//    @JsonProperty("function")
//    private Function function;
//
//    @JsonProperty("function")
//    public Function getFunction() {
//        return function;
//    }
//
//    @JsonProperty("function")
//    public void setFunction(Function function) {
//        this.function = function;
//    }
}
