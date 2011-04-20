package uk.ac.ebi.ebeye;

import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultOfGetResultsIds {

//********************************* VARIABLES ********************************//
    protected ArrayOfString result;
    protected ParamOfGetResultsIds paramOfGetResultsIds;


//******************************** CONSTRUCTORS ******************************//
    public ResultOfGetResultsIds() {
    }

    public ResultOfGetResultsIds(ArrayOfString result, ParamOfGetResultsIds paramOfGetResultsIds) {
        this.result = result;
        this.paramOfGetResultsIds = paramOfGetResultsIds;
    }




//****************************** GETTER & SETTER *****************************//

    public ArrayOfString getResult() {
        return result;
    }

    public void setResult(ArrayOfString result) {
        this.result = result;
    }

    public ParamOfGetResultsIds getParamOfGetResultsIds() {
        return paramOfGetResultsIds;
    }

    public void setParamOfGetResultsIds(ParamOfGetResultsIds paramOfGetResultsIds) {
        this.paramOfGetResultsIds = paramOfGetResultsIds;
    }


//********************************** METHODS *********************************//

}
