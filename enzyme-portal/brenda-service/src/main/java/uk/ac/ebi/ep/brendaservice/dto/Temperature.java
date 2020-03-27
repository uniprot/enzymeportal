package uk.ac.ebi.ep.brendaservice.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Temperature implements Serializable {

    private String ecNumber;
    private String temperatureRange;
    @EqualsAndHashCode.Include
    private String organism;
    private String comment;
    private String accession;

}
