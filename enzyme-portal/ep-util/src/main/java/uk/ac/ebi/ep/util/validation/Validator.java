package uk.ac.ebi.ep.util.validation;


import uk.ac.ebi.ep.search.parameter.SearchParams;


/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Validator {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public static boolean isSearchParamsOK(SearchParams searchParams) {
        boolean ok = false;
        String keywords = searchParams.getKeywords().trim();
        if ( keywords != null) {
            ok = true;
        }
        return ok;
    }


}
