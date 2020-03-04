package uk.ac.ebi.ep.brendaservice.config;

import lombok.Builder;
import lombok.Data;
import org.apache.axis.client.Call;

/**
 *
 * @author joseph
 */
@Builder
@Data
public class BrendaConfig {

    private final Call call;
    private final String parameters;
}
