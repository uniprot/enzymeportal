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
    private String taxId;

    public RelSpecies(String accession, String commonName, String scientificName, BigInteger expEvidenceCode, String taxId) {
        this.accession = accession;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.expEvidenceCode = expEvidenceCode;
        this.taxId = taxId;
    }

    public String getAccession() {
        return accession.trim();
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public BigInteger getExpEvidenceCode() {
        return expEvidenceCode;
    }

    public void setExpEvidenceCode(BigInteger expEvidenceCode) {
        this.expEvidenceCode = expEvidenceCode;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }


}
