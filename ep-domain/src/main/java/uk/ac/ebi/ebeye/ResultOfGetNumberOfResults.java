package uk.ac.ebi.ebeye;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultOfGetNumberOfResults {

//********************************* VARIABLES ********************************//
    protected ParamGetNumberOfResults paramGetNumberOfResults;
    protected int totalFound;


//******************************** CONSTRUCTORS ******************************//
    public ResultOfGetNumberOfResults() {
    }

    public ResultOfGetNumberOfResults(ParamGetNumberOfResults paramGetNumberOfResults, int totalFound) {
        this.paramGetNumberOfResults = paramGetNumberOfResults;
        this.totalFound = totalFound;
    }



//****************************** GETTER & SETTER *****************************//

    public ParamGetNumberOfResults getParamGetNumberOfResults() {
        return paramGetNumberOfResults;
    }

    public void setParamGetNumberOfResults(
            ParamGetNumberOfResults paramGetNumberOfResults) {
        this.paramGetNumberOfResults = paramGetNumberOfResults;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }




//********************************** METHODS *********************************//

}
