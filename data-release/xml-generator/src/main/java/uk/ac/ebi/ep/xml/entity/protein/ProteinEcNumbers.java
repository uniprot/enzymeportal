package uk.ac.ebi.ep.xml.entity.protein;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.xml.entity.Enzyme;
import uk.ac.ebi.ep.xml.transformer.EcNumber;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement

public class ProteinEcNumbers extends Enzyme implements Serializable, Comparator<ProteinEcNumbers> {

    private static final long serialVersionUID = 1L;

    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    //@ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    public ProteinEcNumbers() {
    }

    public ProteinEcNumbers(BigDecimal ecInternalId) {
        this.ecInternalId = ecInternalId;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.ecNumber);
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
        final ProteinEcNumbers other = (ProteinEcNumbers) obj;
        return Objects.equals(this.ecNumber, other.ecNumber);
    }

    @Override
    public String toString() {
        return ecNumber;
    }

    /**
     *
     * @return the enzyme family representation of the ec class
     */
    //@Override
    public String getFamily() {
        Optional<Integer> ec = Optional.ofNullable(this.getEcFamily());
        if (ec.isPresent()) {
            return computeEcToFamilyName(ec.get());
        }
        return "";

    }

    @Override
    public int compare(ProteinEcNumbers ec1, ProteinEcNumbers ec2) {

        return ec1.getEcFamily().compareTo(ec2.getEcFamily());
    }

    public String computeEcToFamilyName(int ec) {

        if (ec == 1) {

            return EcNumber.EnzymeFamily.OXIDOREDUCTASES.getName();
        }
        if (ec == 2) {
            return EcNumber.EnzymeFamily.TRANSFERASES.getName();
        }
        if (ec == 3) {
            return EcNumber.EnzymeFamily.HYDROLASES.getName();
        }
        if (ec == 4) {
            return EcNumber.EnzymeFamily.LYASES.getName();
        }
        if (ec == 5) {
            return EcNumber.EnzymeFamily.ISOMERASES.getName();
        }
        if (ec == 6) {
            return EcNumber.EnzymeFamily.LIGASES.getName();
        }

        return "Invalid Ec Number";
    }

}
