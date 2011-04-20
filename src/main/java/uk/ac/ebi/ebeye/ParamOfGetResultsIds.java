package uk.ac.ebi.ebeye;

import uk.ac.ebi.ep.search.parameter.SearchParams;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 * 
 */
public class ParamOfGetResultsIds {

//********************************* VARIABLES ********************************//
    protected ResultOfGetNumberOfResults resultOfGetNumberOfResults;
        protected SearchParams searchParams;
    
    
//******************************** CONSTRUCTORS ******************************//

    public ParamOfGetResultsIds(){

    }

    public ParamOfGetResultsIds(ResultOfGetNumberOfResults resultOfGetNumberOfResults, SearchParams searchParams) {
        this.resultOfGetNumberOfResults = resultOfGetNumberOfResults;
        this.searchParams = searchParams;
    }



//****************************** GETTER & SETTER *****************************//
    public ResultOfGetNumberOfResults getResultOfGetNumberOfResults() {
        return resultOfGetNumberOfResults;
    }

    public void setResultOfGetNumberOfResults(ResultOfGetNumberOfResults resultOfGetNumberOfResults) {
        this.resultOfGetNumberOfResults = resultOfGetNumberOfResults;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }








//********************************** METHODS *********************************//

}
