package uk.ac.ebi.ep.dataservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Joseph
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProteinFamily {

    private String familyGroupId;
    private String familyName;

}
