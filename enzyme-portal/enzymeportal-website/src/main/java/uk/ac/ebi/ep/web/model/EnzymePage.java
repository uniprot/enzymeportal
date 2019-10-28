package uk.ac.ebi.ep.web.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;
import uk.ac.ebi.ep.literatureservice.model.Result;

/**
 * Represents an enzyme page view
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
    private Set<String> cofactors;
    @Singular
    private Set<String> altNames;
    private final ProteinGroupSearchResult proteins;
    @Singular
    private List<ProteinGroupEntry> associatedProteins;
    private int numProteins;
    @Singular
    private List<Result> citations;
    private int numCitations;

    public List<ProteinGroupEntry> getAssociatedProteins() {
        if (associatedProteins == null) {
            associatedProteins = new ArrayList<>();
        }

        return associatedProteins;
    }

    public Set<String> getCofactors() {
        if (cofactors == null) {
            cofactors = new HashSet<>();
        }
        return cofactors;
    }

    public Set<String> getAltNames() {
        if (altNames == null) {
            altNames = new HashSet<>();
        }

        return altNames;
    }

    public List<Result> getCitations() {
        if (citations == null) {
            citations = new ArrayList<>();
        }

        return citations;
    }

}
