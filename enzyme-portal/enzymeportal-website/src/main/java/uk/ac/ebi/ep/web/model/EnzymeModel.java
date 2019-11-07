package uk.ac.ebi.ep.web.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.ac.ebi.ep.dataservice.common.ProteinModel;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;

/**
 * @author joseph
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnzymeModel extends ProteinModel {

    private String requestedfield;
    private MechanismResult reactionMechanism;

    private List<LabelledCitation> literature;
    private List<String> pathways;
    protected EnzymeEntryPage enzyme;

    public List<LabelledCitation> getLiterature() {
        if (literature == null) {
            literature = new ArrayList<>();
        }
        return literature;
    }

    public void setLiterature(List<LabelledCitation> literature) {
        this.literature = literature;
    }

    public List<String> getPathways() {
        if (pathways == null) {
            pathways = new ArrayList<>();
        }
        return pathways;
    }

    public void setPathways(List<String> pathways) {
        this.pathways = pathways;
    }

}
