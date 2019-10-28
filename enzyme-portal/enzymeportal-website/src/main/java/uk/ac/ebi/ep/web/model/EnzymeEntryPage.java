package uk.ac.ebi.ep.web.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Singular;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EnzymeHierarchy;

/**
 *
 * @author joseph
 */
@Data
public class EnzymeEntryPage {

    private String accession;
    private String function;
    @Singular
    private List<String> synonyms;
    private List<EnzymeHierarchy> enzymeHierarchies;
    private Integer sequence;
    private List<String> provenance;
    


    public List<String> getSynonyms() {
        if (synonyms == null) {
            synonyms = new ArrayList<>();
        }
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<EnzymeHierarchy> getEnzymeHierarchies() {
        if (enzymeHierarchies == null) {
            enzymeHierarchies = new ArrayList<>();
        }
        return enzymeHierarchies;
    }

    public void setEnzymeHierarchies(List<EnzymeHierarchy> enzymeHierarchies) {
        this.enzymeHierarchies = enzymeHierarchies;
    }

    public List<String> getProvenance() {
        if (provenance == null) {
            provenance = new ArrayList<>();
        }
        return provenance;
    }

    public void setProvenance(List<String> provenance) {
        this.provenance = provenance;
    }

}
