package uk.ac.ebi.ep.restapi.dto;

import lombok.Builder;
import lombok.Value;

/**
 *
 * @author joseph
 */
@Value
@Builder
public class Compound {

    private String compoundId;

    private String compoundName;

    private String compoundSource;

}
