package uk.ac.ebi.ep.ebeye.model.enzyme;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
public abstract class AbstractPageView {

    private String enzymeName;
    private long numEnzymeHits;
    private int numProteins;
    private List<ProteinGroupEntry> proteinGroupEntry;

    public List<ProteinGroupEntry> getProteinGroupEntry() {
        if (proteinGroupEntry == null) {
            proteinGroupEntry = new ArrayList<>();
        }
        return proteinGroupEntry;
    }

    public void setProteinGroupEntry(List<ProteinGroupEntry> proteinGroupEntry) {
        this.proteinGroupEntry = proteinGroupEntry;
    }

    public String getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    public long getNumEnzymeHits() {
        return numEnzymeHits;
    }

    public void setNumEnzymeHits(long numEnzymeHits) {
        this.numEnzymeHits = numEnzymeHits;
    }

    public int getNumProteins() {
        return numProteins;
    }

    public void setNumProteins(int numProteins) {
        this.numProteins = numProteins;
    }

}
