package uk.ac.ebi.ep.indexservice.model.protein;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WithPathway {

    String pathwayId;
    String accession;
    String commonName;
    String entryType;
}
