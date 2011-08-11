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

//********************************* VARIABLES ********************************//

    public static final String DEFAULT_SPECIES = "Homo sapiens";
//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public SearchResults getEnzymes(SearchParams searchInput)
            throws EnzymeFinderException;
}
