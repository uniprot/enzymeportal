package uk.ac.ebi.ep.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

/**
 * Protein
 */
@Schema(hidden = true)
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Protein implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Schema(name = "accession", description = "Uniprot accession", example = "P07327", required = true)
    @JsonProperty("accession")
    private String accession;

    @Schema(description = "recommended protein name", example = "Alcohol dehydrogenase")
    @JsonProperty("proteinName")
    private String proteinName;

    @Schema(description = "taxonomy ID", example = "9606")
    @JsonProperty("taxid")
    private long taxid;

    @Schema(description = "organism's scientific name", example = "Homo Sapien")
    @JsonProperty("scientificName")
    private String scientificName;
    @Schema(description = "organism's common name", example = "Human")
    @JsonProperty("commonName")
    private String commonName;

    @Schema(description = "entry type in Uniprot", example = "TREMBL or SwissProt")
    @JsonProperty("entryType")
    private String entryType;

    @Schema(description = "function of the enzyme")
    @JsonProperty("function")
    private String function;
    @Schema(description = "EC number")
    @JsonProperty("ecNumbers")
    @Singular
    List<String> ecNumbers;

    @JsonProperty("sequenceLength")
    @XmlElement(name = "sequenceLength")
    private int sequenceLength;

    @JsonProperty("synonyms")
    @Singular
    private List<String> otherNames;

    @Schema(description = "has Experimental Evidence  code")
    private boolean experimentalEvidence;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Protein {\n");

        sb.append("    accession: ").append(toIndentedString(accession)).append("\n");
        sb.append("    proteinName: ").append(toIndentedString(proteinName)).append("\n");
        sb.append("    taxid: ").append(toIndentedString(taxid)).append("\n");
        sb.append("    scientificName: ").append(toIndentedString(scientificName)).append("\n");
        sb.append("    commonName: ").append(toIndentedString(commonName)).append("\n");
        sb.append("    entryType: ").append(toIndentedString(entryType)).append("\n");
        sb.append("    function: ").append(toIndentedString(function)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
