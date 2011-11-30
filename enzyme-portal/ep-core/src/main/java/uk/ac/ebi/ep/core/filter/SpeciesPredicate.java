package uk.ac.ebi.ep.core.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;

/**
 * Implementation of Predicate which evaluates whether an enzyme summary
 * contains any of the species in the filter. Use it with to build
 * org.apache.commons.collections.collection.PredicatedCollection's.
 * @author rafa
 *
 */
public class SpeciesPredicate implements Predicate {

	/**
	 * Collection of unique species scientific names, which will act as a
	 * filter.
	 */
	private Collection<String> speciesFilter;
	
	public SpeciesPredicate(Collection<String> speciesFilter) {
		this.speciesFilter = speciesFilter;
	}

	/**
	 * Evaluates whether an enzyme summary contains any of the species in the
	 * filter.
	 * @param obj an EnzymeSummary object.
	 * @return <code>true</code> if the enzyme summary contains any of the
	 * 		species in the filter, <code>false</code> otherwise.
	 */
	public boolean evaluate(Object obj) {
		boolean eval = false;
		if (obj instanceof EnzymeSummary){
			EnzymeSummary es = (EnzymeSummary) obj;
			for (EnzymeAccession ea : es.getRelatedspecies()){
				String sciName = ea.getSpecies().getScientificname();
				if (speciesFilter.contains(sciName)){
					eval = true;
					break;
				}
			}
		}
		return eval;
	}

}
