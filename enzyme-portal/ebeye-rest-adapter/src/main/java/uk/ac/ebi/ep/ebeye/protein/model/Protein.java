
package uk.ac.ebi.ep.ebeye.protein.model;

import java.util.Objects;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class Protein implements Comparable<Protein> {

    private String accession;
    private String proteinName;
    private String species;
    private String status;

    public Protein() {
    }
    
    

    /**
     *
     * @param accession
     * @param proteinName
     */
    public Protein(String accession, String proteinName) {
        this.accession = accession;
        this.proteinName = proteinName;
    }

    /**
     * 
     * @param accession
     * @param proteinName
     * @param species 
     */
    public Protein(String accession, String proteinName, String species) {
        this.accession = accession;
        this.proteinName = proteinName;
        this.species = species;
    }

    /**
     * 
     * @param accession
     * @param proteinName
     * @param species
     * @param status reviewed or unreviewed
     */
    public Protein(String accession, String proteinName, String species, String status) {
        this.accession = accession;
        this.proteinName = proteinName;
        this.species = species;
        this.status = status;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getSpecies() {

        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.proteinName);
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
        final Protein other = (Protein) obj;
        return Objects.equals(this.proteinName, other.proteinName);
    }

    @Override
    public String toString() {
        return "Protein{" + "accession=" + accession + ", proteinName=" + proteinName + ", species=" + species + ", status=" + status + '}';
    }

    @Override
    public int compareTo(Protein o) {
        return this.getStatus().compareToIgnoreCase(o.getStatus());
    }

}
