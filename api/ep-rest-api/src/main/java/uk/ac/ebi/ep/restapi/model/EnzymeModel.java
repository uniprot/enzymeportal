package uk.ac.ebi.ep.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 *
 * @author joseph
 */
@Schema(hidden = true)
@XmlRootElement
@Data
@Builder
@Relation(collectionRelation = "enzymes", itemRelation = "enzymeModel")
@JsonPropertyOrder({"enzymeName", "ecNumber", "enzymeFamily", "alternativeNames", "catalyticActivities", "associatedProteins"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EnzymeModel extends RepresentationModel<EnzymeModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "enzyme name", example = "Alcohol dehydrogenase")
    @JsonProperty("enzymeName")
    private String enzymeName;
    @Schema(description = "Enzyme Classfication (EC) number",
            example = "1.1.1.1", required = true)
    @NotBlank
    @EqualsAndHashCode.Include
    @JsonProperty("ecNumber")
    private String ecNumber;
    @JsonProperty("enzymeFamily")
    private String enzymeFamily;
    @JsonProperty("alternativeNames")
    private Set<String> alternativeNames;
    @JsonProperty("catalyticActivities")
    private List<String> catalyticActivities;
    @JsonProperty("associatedProteins")
    @Schema(hidden = true)
    private final CollectionModel<ProteinModel> associatedProteins;

}
