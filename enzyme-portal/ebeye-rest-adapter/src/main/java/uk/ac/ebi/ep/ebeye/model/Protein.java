/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import java.util.Objects;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class Protein {

    private String accession;
    private String proteinName;
    private String species;

    public Protein(String accession, String proteinName) {
        this.accession = accession;
        this.proteinName = proteinName;
    }
    
    

    public Protein(String accession, String proteinName, String species) {
        this.accession = accession;
        this.proteinName = proteinName;
        this.species = species;
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
        return "Protein{" + "accession=" + accession + ", proteinName=" + proteinName + ", species=" + species + '}';
    }

    
}
