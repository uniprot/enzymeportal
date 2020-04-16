package uk.ac.ebi.ep.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.ac.ebi.ep.dataservice.common.ProteinModel;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Kinetics;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.PhDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.TemperatureDependence;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;

/**
 * @author joseph
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnzymeModel extends ProteinModel implements Serializable {

    private String requestedfield;
    private transient MechanismResult reactionMechanism;

    private transient List<LabelledCitation> literature;
    private List<String> pathways;
    protected transient EnzymeEntryPage enzyme;

    private transient Kinetics kinetics;
    private transient List<PhDependence> phDependences;
    private transient List<TemperatureDependence> temperatureDependences;

    public List<PhDependence> getPhDependences() {
        if (phDependences == null) {
            phDependences = new ArrayList<>();
        }
        return phDependences;
    }

    public void setPhDependences(List<PhDependence> phDependences) {
        this.phDependences = phDependences;
    }

    public List<TemperatureDependence> getTemperatureDependences() {
        if (temperatureDependences == null) {
            temperatureDependences = new ArrayList<>();
        }

        return temperatureDependences;
    }

    public void setTemperatureDependences(List<TemperatureDependence> temperatureDependences) {
        this.temperatureDependences = temperatureDependences;
    }

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
