package uk.ac.ebi.ep.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.ep.indexservice.model.protein.PdbImage;

/**
 *
 * @author joseph
 */
@Schema(hidden = true)
@XmlRootElement
@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProteinSummary extends RepresentationModel<ProteinSummary> {//extends ProteinResult  {

    @EqualsAndHashCode.Include
    @Schema(name = "accession", description = "Uniprot accession", example = "P07327", required = true)
    @JsonProperty("accession")
    private final String accession;

    @Schema(description = "recommended protein name", example = "Alcohol dehydrogenase")
    @JsonProperty("proteinName")
    private final String proteinName;

    @Schema(description = "organism's name", example = "Human")
    @JsonProperty("organismName")
    private final String organismName;

    private final List<String> geneName;

    private final PdbImage pdbAccession;

    private final String function;

    private final List<String> synonyms;

    private final List<String> diseases;

    private final List<String> ecNumbers;
    private final List<String> catalyticActivities;

}
