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
public class Sequence implements Serializable {

    private String ecNumber;
    private String organism;
     @EqualsAndHashCode.Include
    private String accession;



}
