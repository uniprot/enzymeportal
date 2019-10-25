package uk.ac.ebi.ep.indexservice.model.protein;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelSpecies {

    private String accession = "";
    private String commonName = "";
    private String scientificName = "";
    private BigInteger expEvidenceCode;
    private String taxId;

}
