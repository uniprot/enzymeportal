package uk.ac.ebi.ep.core;

import uk.ac.ebi.ep.search.model.Species;

/**
 * Wrapper around a Species object which provides the missing
 * <code>equals</code> and <code>hashCode</code> methods (the class
 * <code>Species</code> is auto-generated from a XML schema and does not
 * provide them).
 * <br>
 * It also implements <code>Comparable</code> to provide a 'natural' order for
 * Species objects.
 * <br>
 * All the implemented methods are based exclusively on the species scientific
 * name.
 * @author rafa
 *
 */
public class SpeciesDefaultWrapper implements Comparable<SpeciesDefaultWrapper> {

    private Species species;

    public Species getSpecies() {
        return species;
    }

    public SpeciesDefaultWrapper(Species species) {
        if (species == null) {
            throw new NullPointerException();
        }
        this.species = species;
    }

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + species.getScientificname().hashCode();
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) return true;
//		if (obj == null) return false;
//		if (getClass() != obj.getClass()) return false;
//		SpeciesDefaultWrapper other = (SpeciesDefaultWrapper) obj;
//		if (!species.getScientificname().equals(other.species.getScientificname()))
//			return false;
//		return true;
//	}
    public int compareTo(SpeciesDefaultWrapper other) {
        // TODO: put Homo sapiens and others at the top.
        //return species.getScientificname().compareTo(other.species.getScientificname());
        return species.getCommonname().compareTo(other.species.getCommonname());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpeciesDefaultWrapper other = (SpeciesDefaultWrapper) obj;
        if (this.species != other.species && (this.species == null || !this.species.equals(other.species))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.species != null ? this.species.hashCode() : 0);
        return hash;
    }
}
