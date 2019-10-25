package uk.ac.ebi.ep.comparisonservice.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

/**
 *
 * @author joseph
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionPathway {

    private Reaction reaction;
    @Singular
    private List<String> pathways = new ArrayList<>();

}
