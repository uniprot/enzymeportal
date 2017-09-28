package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import lombok.Data;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
public class RelSpecies {

    private String accession;
    private String commonName;
    private String scientificName;

    public RelSpecies(String accession, String commonName, String scientificName) {
        this.accession = accession;
        this.commonName = commonName;
        this.scientificName = scientificName;
    }

//    public String getAccession() {
//        return accession;
//    }
//
//    public void setAccession(String accession) {
//        this.accession = accession;
//    }
//
//    public String getCommonName() {
//        return commonName;
//    }
//
//    public void setCommonName(String commonName) {
//        this.commonName = commonName;
//    }
//
//    public String getScientificName() {
//        return scientificName;
//    }
//
//    public void setScientificName(String scientificName) {
//        this.scientificName = scientificName;
//    }

    @Override
    public String toString() {
        return "RelSpecies{" + "accession=" + accession + ", commonName=" + commonName + ", scientificName=" + scientificName + '}';
    }

}
