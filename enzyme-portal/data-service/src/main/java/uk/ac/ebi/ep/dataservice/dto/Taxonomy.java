package uk.ac.ebi.ep.dataservice.dto;

import java.math.BigInteger;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 * Taxonomy is a data transfer object for model organisms
 *
 * @author joseph
 */
public class Taxonomy implements Comparable<Taxonomy> {

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
    public String toString() {
        return "Taxonomy{" + "taxId=" + taxId + ", scientificName=" + scientificName + ", commonName=" + commonName + ", num_enzymes=" + numEnzymes + '}';
    }

    @Override
    public int compareTo(Taxonomy other) {
        return taxId.compareTo(other.getTaxId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.taxId);
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
        final Taxonomy other = (Taxonomy) obj;
        return Objects.equals(this.taxId, other.taxId);
    }

}
