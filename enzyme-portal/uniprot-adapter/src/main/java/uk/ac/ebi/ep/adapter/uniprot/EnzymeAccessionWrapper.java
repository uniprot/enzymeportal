/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.adapter.uniprot;

import uk.ac.ebi.ep.search.model.EnzymeAccession;

/**
 *
 * @author joseph
 */
public class EnzymeAccessionWrapper implements Comparable<EnzymeAccession> {

    private final EnzymeAccession accession;

    public EnzymeAccessionWrapper(  EnzymeAccession accession) {
        this.accession = accession;
    }

    public final EnzymeAccession getAccession() {
        return accession;
    }

    public int compareTo(EnzymeAccession other) {
//            if (accession.getSpecies().getCommonname() == null | ez.getSpecies().getCommonname() == null) {
//                return accession.getSpecies().getScientificname().compareTo(ez.getSpecies().getScientificname());
//            }
//            return accession.getSpecies().getCommonname().compareTo(ez.getSpecies().getCommonname());
//            
            
        if (accession.getSpecies().getCommonname() == null && other.getSpecies().getCommonname() == null) {
            return accession.getSpecies().getScientificname().compareTo(other.getSpecies().getScientificname());
        }
        if (accession.getSpecies().getCommonname() != null && other.getSpecies().getCommonname() == null) {
            return accession.getSpecies().getCommonname().compareTo(other.getSpecies().getScientificname());
        }
        if (accession.getSpecies().getCommonname() == null && other.getSpecies().getCommonname() != null) {
            return accession.getSpecies().getScientificname().compareTo(other.getSpecies().getCommonname());
        }

        return accession.getSpecies().getCommonname().compareTo(other.getSpecies().getCommonname());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymeAccessionWrapper other = (EnzymeAccessionWrapper) obj;
        if (this.accession != other.accession && (this.accession == null || !this.accession.equals(other.accession))) {
            return false;
        }

        if (this.accession.getSpecies().getCommonname() != other.accession.getSpecies().getCommonname() && (this.accession.getSpecies().getCommonname() == null || !this.accession.getSpecies().getCommonname().equals(other.accession.getSpecies().getCommonname()))) {
            return false;
        }
        if (this.accession.getSpecies().getScientificname() != other.accession.getSpecies().getScientificname() && (this.accession.getSpecies().getScientificname() == null || !this.accession.getSpecies().getScientificname().equals(other.accession.getSpecies().getScientificname()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 37 * hash + (this.accession.getSpecies().getScientificname() != null ? this.accession.getSpecies().getScientificname().hashCode() : 0);
        //hash = 23 * hash + (this.accession != null ? this.accession.hashCode() : 0);
        // hash = 21 * hash + (this.accession.getSpecies().getCommonname() != null ? this.accession.getSpecies().getCommonname().hashCode() : 0);
        return hash;
    }
}
