package uk.ac.ebi.ebeye;

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
    protected int start;
    protected int size;
    
    
//******************************** CONSTRUCTORS ******************************//

    public ParamOfGetResultsIds(){

    }

    public ParamOfGetResultsIds(ResultOfGetNumberOfResults
                    resultOfGetNumberOfResults, int start, int size) {
        this.resultOfGetNumberOfResults = resultOfGetNumberOfResults;
        this.size = size;
        this.start = start;
    }
//****************************** GETTER & SETTER *****************************//


    public ResultOfGetNumberOfResults getResultOfGetNumberOfResults() {
        return resultOfGetNumberOfResults;
    }

    public void setResultOfGetNumberOfResults(ResultOfGetNumberOfResults resultOfGetNumberOfResults) {
        this.resultOfGetNumberOfResults = resultOfGetNumberOfResults;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }


}
