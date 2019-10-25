package uk.ac.ebi.ep.comparisonservice.model;

import java.util.ArrayList;
import java.util.List;
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
public class Disease {

    private String id;
    private String name;
    private String url;
    private List<String> evidences = new ArrayList<>();
}
