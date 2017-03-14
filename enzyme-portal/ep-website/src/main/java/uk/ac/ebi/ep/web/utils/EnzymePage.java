package uk.ac.ebi.ep.web.utils;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ep.literatureservice.model.Result;

/**
 * Represents an enzyme view
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Data
@Builder(builderMethodName = "enzymePageBuilder")
public class EnzymePage {

    //@Singular
    private final List<String> accessions;
    private String enzymeName;
    private String ec;
    private String catalyticActivities;
    private final List<String> cofactor;
    private final List<String> altName;
    //@Singular
    private List<Result> citations;

}
