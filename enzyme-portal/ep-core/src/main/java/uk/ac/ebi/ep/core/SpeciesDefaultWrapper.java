package uk.ac.ebi.ep.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
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
public final class SpeciesDefaultWrapper implements Comparable<SpeciesDefaultWrapper> {

    String[] commonSpecies = {"Human", "Mouse", "Rat", "Fruit fly", "Worm", "Yeast", "Ecoli"};
    List<String> commonSpecieList = Arrays.asList(commonSpecies);
    // Map<Integer, Species> priorityMapper = new TreeMap<Integer, Species>();
    private final Species species;

    public final Species getSpecies() {
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

        if (species.getCommonname() == null || other.species.getCommonname() == null) {
            return species.getScientificname().compareTo(other.species.getScientificname());
        }
        if (species.getCommonname() != null && other.species.getCommonname() == null) {
            return species.getCommonname().compareTo(other.species.getScientificname());
        }
        if (species.getCommonname() == null && other.species.getCommonname() != null) {
            return species.getScientificname().compareTo(other.species.getCommonname());
        }
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
        if (this.species.getCommonname() != other.species.getCommonname() && (this.species.getCommonname() == null || !this.species.getCommonname().equals(other.species.getCommonname()))) {
            return false;
        }
        if (this.species.getScientificname() != other.species.getScientificname() && (this.species.getScientificname() == null || !this.species.getScientificname().equals(other.species.getScientificname()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.species.getScientificname() != null ? this.species.getScientificname().hashCode() : 0);
        return hash;
    }
    AtomicInteger key = new AtomicInteger(8);

    public List<Species> build() {
       
        Map<Integer, Species> priorityMapper = new TreeMap<Integer, Species>();
        if (commonSpecieList.contains(species.getCommonname())) {
            // Human, Mouse, Rat, Fly, Worm, Yeast, Ecoli 
            if (species.getCommonname().equalsIgnoreCase("Human")) {
                priorityMapper.put(1, species);
            } else if (species.getCommonname().equalsIgnoreCase("Mouse")) {
                priorityMapper.put(2, species);
            } else if (species.getCommonname().equalsIgnoreCase("Rat")) {
                priorityMapper.put(3, species);
            } else if (species.getCommonname().equalsIgnoreCase("Fruit fly")) {
                priorityMapper.put(4, species);
            } else if (species.getCommonname().equalsIgnoreCase("Worm")) {
                priorityMapper.put(5, species);
            } else if (species.getCommonname().equalsIgnoreCase("Yeast")) {
                priorityMapper.put(6, species);
            } else if (species.getCommonname().equalsIgnoreCase("Ecoli")) {
                priorityMapper.put(7, species);
            }
        } else {

            priorityMapper.put(key.getAndIncrement(), species);

        }
             List<Species> speciesFilters = new LinkedList<Species>();
        for (Map.Entry<Integer, Species> map : priorityMapper.entrySet()) {
            speciesFilters.add(map.getValue());
                       
        }

        return speciesFilters;
    }

}
