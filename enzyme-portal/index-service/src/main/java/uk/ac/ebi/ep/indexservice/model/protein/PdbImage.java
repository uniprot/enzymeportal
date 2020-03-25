package uk.ac.ebi.ep.indexservice.model.protein;

import lombok.Data;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
public class PdbImage {

    private String pdbId;
    private String specie;
    private String accession;

    public PdbImage() {
    }

    public PdbImage(String pdbId, String specie) {
        this.pdbId = pdbId;
        this.specie = specie;
    }

    public PdbImage(String pdbId, String specie, String accession) {
        this.pdbId = pdbId;
        this.specie = specie;
        this.accession = accession;
    }

    @Override
    public String toString() {
        return "PdbImage{" + "pdbId=" + pdbId + ", specie=" + specie + ", accession=" + accession + '}';
    }
    
    

}
