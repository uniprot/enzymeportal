package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IEnzymeFinder {

	/**
	 * Resources used to search/retrieve enzymes in UniProt.
	 * @author rafa
	 */
	public static enum UniprotSource { EBEYE, UNIPROT }
	
	/**
	 * Implementation used to search/retrieve enzymes from UniProt.
	 * @author rafa
	 */
	public static enum UniprotImplementation { JAPI, WS }

	public SearchResults getEnzymes(SearchParams searchInput)
            throws EnzymeFinderException;
}
