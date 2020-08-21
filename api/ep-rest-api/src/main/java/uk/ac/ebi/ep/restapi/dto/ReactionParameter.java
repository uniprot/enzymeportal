package uk.ac.ebi.ep.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Kinetics;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.PhDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.TemperatureDependence;

/**
 *
 * @author joseph
 */
@Value
@Builder
public class ReactionParameter {

    @JsonProperty("kinetics")
    private final Kinetics kinetics;
    @Singular(ignoreNullCollections = true, value = "phDependence")
    @JsonProperty("phDependence")
    private final List<PhDependence> phDependence;
    @Singular(ignoreNullCollections = true, value = "temperatureDependence")
    @JsonProperty("temperatureDependence")
    private final List<TemperatureDependence> temperatureDependence;
}
