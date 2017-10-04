package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import lombok.Data;


/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
public class RelSpecies  {

    private String accession;
    private String commonName;
    private String scientificName;
    private int expEvidenceCode;

    public RelSpecies(String accession, String commonName, String scientificName, int expEvidenceCode) {
        this.accession = accession;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.expEvidenceCode = expEvidenceCode;
    }





}
