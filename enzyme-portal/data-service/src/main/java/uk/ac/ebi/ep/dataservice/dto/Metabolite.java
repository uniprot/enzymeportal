package uk.ac.ebi.ep.dataservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
@AllArgsConstructor
public class Metabolite {

    protected String metaboliteId;
    protected String metaboliteName;
}
