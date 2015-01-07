package uk.ac.ebi.ep.base.common;

import java.util.Collection;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;



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
	private final Collection<String> speciesFilter;
	
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
		if (speciesFilter == null || speciesFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof UniprotEntry){
			UniprotEntry es = (UniprotEntry) obj;
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
