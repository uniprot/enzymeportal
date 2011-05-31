package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.search.parameter.SearchParams;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class JUnitInit {

//********************************* VARIABLES ********************************//
    public static final String SEARCH_KEYWORDS = "ethanol";
    public static final int START = 0;
    public static final int SIZE = 10;
    public static final int TOTAL_DOMAINS = 5;
//******************************** CONSTRUCTORS ******************************//

//****************************** GETTER & SETTER *****************************//
//********************************** METHODS *********************************//
    public static SearchParams initSearchParam() {
        SearchParams searchParams = new SearchParams();
        searchParams.setKeywords(SEARCH_KEYWORDS);
        searchParams.setStart(START);
        searchParams.setSize(SIZE);
        return searchParams;
    }
}
