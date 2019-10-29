package uk.ac.ebi.ep.dataservice.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import uk.ac.ebi.ep.dataservice.dto.Species;

/**
 *
 * @author joseph
 */
@Data
public class RelatedSpecie implements Serializable {

    private List<String> uniprotaccessions;

    protected Species species;
    protected Long taxId;
    protected String accession;
    protected Boolean expEvidence;

    public List<String> getUniprotaccessions() {
        if (uniprotaccessions == null) {
            uniprotaccessions = new ArrayList<>();
        }
        return uniprotaccessions;
    }

    public void setUniprotaccessions(List<String> uniprotaccessions) {
        this.uniprotaccessions = uniprotaccessions;
    }

    @Override
    public String toString() {
        return "EnzymeAccession{" + "species=" + species + ", expEvidence=" + expEvidence + ", uniprotaccession=" + accession + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.species);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelatedSpecie other = (RelatedSpecie) obj;
        return Objects.equals(this.species, other.species);
    }

}
