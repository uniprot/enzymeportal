package uk.ac.ebi.ep.web.utils;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ep.data.domain.IntenzAltNames;
import uk.ac.ebi.ep.data.domain.IntenzCofactors;
import uk.ac.ebi.ep.data.entry.AssociatedProtein;
import uk.ac.ebi.ep.literatureservice.model.Result;

/**
 * Represents an enzyme view
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Data
@Builder(builderMethodName = "enzymePageBuilder")
public class EnzymePage {

    private String enzymeName;
    private String ec;
    private String catalyticActivities;
    private final Set<IntenzCofactors> cofactors;
    private final Set<IntenzAltNames> altNames;
    //@Singular
    private final List<AssociatedProtein> proteins;
    private List<Result> citations;

}
