package uk.ac.ebi.ep.comparisonservice.model;

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
public class Compound {

    private String id;
    private String name;
    private String source;
    private String url;
    private String role;

}
