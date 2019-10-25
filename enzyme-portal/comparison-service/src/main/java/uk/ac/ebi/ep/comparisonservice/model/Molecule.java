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
@NoArgsConstructor
@AllArgsConstructor
public class Molecule {

    private List<Compound> cofactors = new ArrayList<>();
    private List<Compound> inhibitors = new ArrayList<>();
    private List<Compound> activators = new ArrayList<>();
}
