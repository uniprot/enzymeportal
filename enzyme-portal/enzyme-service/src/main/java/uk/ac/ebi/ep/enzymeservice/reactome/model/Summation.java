package uk.ac.ebi.ep.enzymeservice.reactome.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dbId",
    "displayName",
    "text",
    "schemaClass",
    "className"
})
/**
 *
 * @author joseph
 */
@Data
public class Summation {

    @JsonProperty("dbId")
    private Integer dbId;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("text")
    private String text;
    @JsonProperty("schemaClass")
    private String schemaClass;
    @JsonProperty("className")
    private String className;

    @Override
    public String toString() {
        return "Summation{" + "dbId=" + dbId + ", displayName=" + displayName + ", text=" + text + ", schemaClass=" + schemaClass + ", className=" + className + '}';
    }

}
