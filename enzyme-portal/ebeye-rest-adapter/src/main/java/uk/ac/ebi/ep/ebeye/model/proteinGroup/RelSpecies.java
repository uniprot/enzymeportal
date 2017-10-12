package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import java.math.BigInteger;
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
    private BigInteger expEvidenceCode;

    public RelSpecies(String accession, String commonName, String scientificName, BigInteger expEvidenceCode) {
        this.accession = accession;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.expEvidenceCode = expEvidenceCode;
    }

    public String getAccession() {
        return accession.trim();
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }


}
