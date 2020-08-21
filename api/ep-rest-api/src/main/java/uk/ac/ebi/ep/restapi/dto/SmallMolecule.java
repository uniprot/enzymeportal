package uk.ac.ebi.ep.restapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 *
 * @author joseph
 */
@Value
@Builder
public class SmallMolecule {

    @Singular(ignoreNullCollections = true, value = "cofactors")
    private List<Compound> cofactors;
    @Singular(ignoreNullCollections = true, value = "inhibitors")
    private List<Compound> inhibitors;
    @Singular(ignoreNullCollections = true, value = "activators")
    private List<Compound> activators;
}
