package uk.ac.ebi.ep.enzymeservice.reactome.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dbId",
    "displayName",
    "url",
    "schemaClass",
    "className"
})
/**
 *
 * @author joseph
 */
@Data
public class Figure {

    @JsonProperty("dbId")
    private Integer dbId;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("url")
    private String url;
    @JsonProperty("schemaClass")
    private String schemaClass;
    @JsonProperty("className")
    private String className;

   

}
