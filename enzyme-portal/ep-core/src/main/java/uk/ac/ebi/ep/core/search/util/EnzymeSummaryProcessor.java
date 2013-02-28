package uk.ac.ebi.ep.core.search.util;

import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 * Objects implementing this interface process {@link EnzymeSummary}s in a
 * certain way.
 * This is intended to process items in a collection, using more than one
 * processor and hence avoiding repeated looping.
 * @author rafa
 */
public interface EnzymeSummaryProcessor {

	/**
	 * Processes the summary, perhaps modifying it somehow.
	 * @param item
	 */
	public void process(EnzymeSummary item);
}
