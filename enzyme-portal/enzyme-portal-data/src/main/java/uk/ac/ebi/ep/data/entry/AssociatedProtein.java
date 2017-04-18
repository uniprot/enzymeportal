package uk.ac.ebi.ep.data.entry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class AssociatedProtein {

    private String proteinName;
    private String accession;
    private String commonName;

    public AssociatedProtein(String proteinName, String accession, String commonName) {
        this.proteinName = proteinName;
        this.accession = accession;
        this.commonName = commonName;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public String toString() {
        return "AssociatedProtein{" + " proteinName=" + proteinName + ", accession=" + accession + ", commonName=" + commonName + '}';
    }

}
