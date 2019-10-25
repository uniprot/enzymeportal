package uk.ac.ebi.ep.enzymeservice.intenz.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joseph
 *
 */
public class EnzymeHierarchy {

    private List<EcClass> ecclass;

    public List<EcClass> getEcclass() {
        if (ecclass == null) {
            ecclass = new ArrayList<>();
        }
        return this.ecclass;
    }

    public void setEcclass(List<EcClass> ecclass) {
        this.ecclass = ecclass;
    }

}
