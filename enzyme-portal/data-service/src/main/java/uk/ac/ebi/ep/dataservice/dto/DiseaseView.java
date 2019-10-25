package uk.ac.ebi.ep.dataservice.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joseph
 */
public interface DiseaseView {

    String getOmimNumber();

    String getDiseaseName();

    String getDescription();

    String getEvidence();

    String getUrl();

    default List<String> getEvidences() {
        List<String> evidences = new ArrayList<>();
        evidences.add(getEvidence());
        return evidences;
    }

}
