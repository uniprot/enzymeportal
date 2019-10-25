package uk.ac.ebi.ep.enzymeservice.reactome.model;

/**
 *
 * @author joseph
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dbId",
    "displayName",
    "url",
    "schemaClass",
    "className"
})
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

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
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

}
