package uk.ac.ebi.ep.dataservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cofactor {

    private String compoundId;
    private String compoundName;

}
