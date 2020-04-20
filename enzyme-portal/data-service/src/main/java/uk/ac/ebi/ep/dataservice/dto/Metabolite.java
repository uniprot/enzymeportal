package uk.ac.ebi.ep.dataservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Metabolite {

    @EqualsAndHashCode.Include
    private String metaboliteId;
    private String metaboliteName;
}
