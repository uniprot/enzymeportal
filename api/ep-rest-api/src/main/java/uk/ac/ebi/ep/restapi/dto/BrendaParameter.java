package uk.ac.ebi.ep.restapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import uk.ac.ebi.ep.brendaservice.dto.Brenda;
import uk.ac.ebi.ep.brendaservice.dto.Ph;
import uk.ac.ebi.ep.brendaservice.dto.Temperature;

/**
 *
 * @author joseph
 */
@Value
@Builder
public class BrendaParameter {

    @Singular(ignoreNullCollections = true, value = "kinectics")
    private List<Brenda> kinectics;
    @Singular(ignoreNullCollections = true, value = "ph")
    private List<Ph> ph;
    @Singular(ignoreNullCollections = true, value = "temperature")
    private List<Temperature> temperature;
}
