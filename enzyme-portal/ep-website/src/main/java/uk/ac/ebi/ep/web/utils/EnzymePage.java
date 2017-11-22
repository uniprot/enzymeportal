package uk.ac.ebi.ep.web.utils;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;
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
    @Singular
    private Set<String> cofactors;// = new HashSet<>();
    @Singular
    private final Set<String> altNames;
    //@Singular
    private final ProteinGroupSearchResult proteins;
    private int numProteins;
    @Singular
    private List<Result> citations;
    private int numCitations;

}
