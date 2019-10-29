package uk.ac.ebi.ep.enzymeservice.reactome.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


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

    @JsonProperty("dbId")
    public Integer getDbId() {
        return dbId;
    }

    @JsonProperty("dbId")
    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("schemaClass")
    public String getSchemaClass() {
        return schemaClass;
    }

    @JsonProperty("schemaClass")
    public void setSchemaClass(String schemaClass) {
        this.schemaClass = schemaClass;
    }

    @JsonProperty("className")
    public String getClassName() {
        return className;
    }

    @JsonProperty("className")
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Summation{" + "dbId=" + dbId + ", displayName=" + displayName + ", text=" + text + ", schemaClass=" + schemaClass + ", className=" + className + '}';
    }

}
