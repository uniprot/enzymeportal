package uk.ac.ebi.ep.comparisonservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.ac.ebi.ep.dataservice.common.ProteinModel;

/**
 * @author joseph
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComparisonProteinModel extends ProteinModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> ec;
    private List<ReactionPathway> reactionpathway;
    private Molecule molecule;
    private List<Disease> diseases;

    public List<String> getEc() {
        if (ec == null) {
            ec = new ArrayList<>();
        }
        return ec;
    }

    public List<ReactionPathway> getReactionpathway() {
        if (reactionpathway == null) {
            reactionpathway = new ArrayList<>();
        }
        return reactionpathway;
    }

    public List<Disease> getDiseases() {
        if (diseases == null) {
            diseases = new ArrayList<>();
        }
        return diseases;
    }

}
