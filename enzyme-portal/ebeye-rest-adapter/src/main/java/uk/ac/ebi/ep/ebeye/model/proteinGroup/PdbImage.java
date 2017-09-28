
package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import lombok.Data;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
public class PdbImage {
    private String pdbId;
    private String specie;

    public PdbImage() {
    }
    
    

    public PdbImage(String pdbId, String specie) {
        this.pdbId = pdbId;
        this.specie = specie;
    }
    
}
