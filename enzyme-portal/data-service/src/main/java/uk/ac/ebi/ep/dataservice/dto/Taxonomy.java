package uk.ac.ebi.ep.dataservice.dto;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * Taxonomy is a data transfer object for model organisms
 *
 * @author joseph
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Taxonomy implements Comparable<Taxonomy> {

    @EqualsAndHashCode.Include

    private Long taxId;

    private String scientificName;

    private String commonName;
    private BigInteger numEnzymes;

    public Taxonomy(Long taxId, String scientificName, String commonName) {
        this.taxId = taxId;
        this.scientificName = scientificName;
        this.commonName = commonName;
    }

    public Taxonomy(Long taxId, String scientificName, String commonName, BigInteger numEnzymes) {
        this.taxId = taxId;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.numEnzymes = numEnzymes;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        if (StringUtils.isEmpty(commonName) && "Escherichia coli (strain K12)".equalsIgnoreCase(scientificName)) {
            commonName = "E.Coli";
        }

        if (StringUtils.isEmpty(commonName) && "Bacillus subtilis (strain 168)".equalsIgnoreCase(scientificName)) {
            commonName = "Bacillus subtilis";
        }

        if (StringUtils.isEmpty(commonName)) {
            return scientificName;
        }

        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public BigInteger getNumEnzymes() {
        return numEnzymes;
    }

    public void setNumEnzymes(BigInteger numEnzymes) {
        this.numEnzymes = numEnzymes;
    }

    @Override
    public int compareTo(Taxonomy other) {
        return taxId.compareTo(other.getTaxId());
    }


}
