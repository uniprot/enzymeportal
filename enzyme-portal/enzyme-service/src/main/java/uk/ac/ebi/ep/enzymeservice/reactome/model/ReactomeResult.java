package uk.ac.ebi.ep.enzymeservice.reactome.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 *
 * @author joseph
 */
public class ReactomeResult {

    @JsonProperty("dbId")
    private Integer dbId;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("stId")
    private String stId;
    @JsonProperty("stIdVersion")
    private String stIdVersion;
    @JsonProperty("isInDisease")
    private Boolean isInDisease;
    @JsonProperty("isInferred")
    private Boolean isInferred;
    @JsonProperty("name")
    private List<String> name = null;
    @JsonProperty("releaseDate")
    private String releaseDate;
    @JsonProperty("speciesName")
    private String speciesName;
    @JsonProperty("figure")
    private List<Figure> figure = null;
    @JsonProperty("summation")
    private List<Summation> summation = null;
    @JsonProperty("hasDiagram")
    private Boolean hasDiagram;
    @JsonProperty("hasEHLD")
    private Boolean hasEHLD;
    @JsonProperty("schemaClass")
    private String schemaClass;
    @JsonProperty("className")
    private String className;

    public ReactomeResult() {
    }

    @JsonProperty("summation")
    public List<Summation> getSummation() {
        if (summation == null) {
            summation = new ArrayList<>();
        }
        return summation;
    }

    @JsonProperty("figure")
    public List<Figure> getFigure() {
        if (figure == null) {
            figure = new ArrayList<>();
        }

        return figure;
    }

    @Override
    public String toString() {
        return "ReactomeResult{" + "dbId=" + dbId + ", displayName=" + displayName + ", stId=" + stId + ", stIdVersion=" + stIdVersion + ", isInDisease=" + isInDisease + ", isInferred=" + isInferred + ", name=" + name + ", releaseDate=" + releaseDate + ", speciesName=" + speciesName + ", summation=" + summation + ", hasDiagram=" + hasDiagram + ", hasEHLD=" + hasEHLD + ", schemaClass=" + schemaClass + ", className=" + className + '}';
    }

}
