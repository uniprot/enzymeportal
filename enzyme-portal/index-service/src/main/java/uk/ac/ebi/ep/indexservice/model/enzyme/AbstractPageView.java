package uk.ac.ebi.ep.indexservice.model.enzyme;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Getter
@Setter
public abstract class AbstractPageView {

    private String enzymeName;
    private long numEnzymeHits;
    private int numProteins;
    @Singular
    private List<ProteinGroupEntry> proteinGroupEntry;

    public List<ProteinGroupEntry> getProteinGroupEntry() {
        if (proteinGroupEntry == null) {
            proteinGroupEntry = new ArrayList<>();
        }
        return proteinGroupEntry;
    }

}
