package uk.ac.ebi.ep.base.common;

import java.util.Collection;
import org.apache.commons.collections.Predicate;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;

/**
 * Implementation of Predicate which evaluates whether an enzyme summary
 * contains any of the compounds in the filter. Use it with to build
 * org.apache.commons.collections.collection.PredicatedCollection's.
 *
 * @author joseph
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
     *
     * @param obj an EnzymeSummary object.
     * @return <code>true</code> if the enzyme summary contains any of the
     * compounds in the filter, <code>false</code> otherwise.
     */
    @Override
    public boolean evaluate(Object obj) {
        if (compoundsFilter == null || compoundsFilter.isEmpty()) {
            return true;
        }
        boolean eval = false;
        if (obj instanceof UniprotEntry) {
            UniprotEntry es = (UniprotEntry) obj;
            for (EnzymeAccession ea : es.getRelatedspecies()) {
                for (Compound compound : ea.getCompounds()) {
                    String id = compound.getId();
                    if (compoundsFilter.contains(id)) {
                        eval = true;
                        break;
                    }
                }
            }
        }
        return eval;
    }

}
