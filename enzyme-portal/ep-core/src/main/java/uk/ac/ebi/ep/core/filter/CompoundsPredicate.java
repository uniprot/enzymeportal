package uk.ac.ebi.ep.core.filter;

import java.util.Collection;

import org.apache.commons.collections.Predicate;

import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 * Implementation of Predicate which evaluates whether an enzyme summary
 * contains any of the compounds in the filter. Use it with to build
 * org.apache.commons.collections.collection.PredicatedCollection's.
 * @author rafa
 *
 */
public class CompoundsPredicate implements Predicate {

	/**
	 * Collection of unique compound IDs, which will act as a filter.
	 */
	Collection<String> compoundsFilter;
	
	public CompoundsPredicate(Collection<String> compoundsFilter) {
		this.compoundsFilter = compoundsFilter;
	}

	/**
	 * Evaluates whether an enzyme summary contains any of the compounds in the
	 * filter.
	 * @param obj an EnzymeSummary object.
	 * @return <code>true</code> if the enzyme summary contains any of the
	 * 		compounds in the filter, <code>false</code> otherwise.
	 */
	public boolean evaluate(Object obj) {
		if (compoundsFilter == null || compoundsFilter.isEmpty()){
			return true;
		}
		boolean eval = false;
		if (obj instanceof EnzymeSummary){
			EnzymeSummary es = (EnzymeSummary) obj;
			for (EnzymeAccession ea : es.getRelatedspecies()){
				for (Compound compound : ea.getCompounds()) {
					String id = compound.getId();
					if (compoundsFilter.contains(id)){
						eval = true;
						break;
					}
				}
			}
		}
		return eval;
	}

}
