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
public class Brenda implements Serializable {

    private String ecNumber;
     @EqualsAndHashCode.Include
    private String organism;
    private String kcatKmValue;
    private String kmValue;
    private String substrate;

    private String comment;

    private boolean kmv;
    private String accession;



}
