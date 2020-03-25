package uk.ac.ebi.reaction.mechanism.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

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
@Data
public class Role {

    @JsonProperty("group_function")
    private String groupFunction;
    @JsonProperty("function_type")
    private String functionType;
    @JsonProperty("function")
    private String function;
    @JsonProperty("emo")
    private String emo;


}
