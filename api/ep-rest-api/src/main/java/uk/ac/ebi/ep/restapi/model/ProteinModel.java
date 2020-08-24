package uk.ac.ebi.ep.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 *
 * @author joseph
 */
@Schema(hidden = true)
@XmlRootElement
@Builder
@Getter
@JsonPropertyOrder({"accession", "proteinName", "organismName"})
@Relation(collectionRelation = "proteins", itemRelation = "proteinModel")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProteinModel extends RepresentationModel<ProteinModel> {

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

}
