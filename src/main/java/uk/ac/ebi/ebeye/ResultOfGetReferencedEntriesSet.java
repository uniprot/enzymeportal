package uk.ac.ebi.ebeye;

import java.util.List;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultOfGetReferencedEntriesSet {

//********************************* VARIABLES ********************************//
protected ResultOfGetResultsIds resultOfGetResultsIds;
protected List<String> uniprotXRefList;


//******************************** CONSTRUCTORS ******************************//
    public ResultOfGetReferencedEntriesSet(){
    }
    
    public ResultOfGetReferencedEntriesSet(
            ResultOfGetResultsIds resultOfGetResultsIds,
            List<String> uniprotXRefList) {
        this.resultOfGetResultsIds = resultOfGetResultsIds;
        this.uniprotXRefList = uniprotXRefList;
    }

//****************************** GETTER & SETTER *****************************//


    public ResultOfGetResultsIds getResultOfGetResultsIds() {
        return resultOfGetResultsIds;
    }

    public void setResultOfGetResultsIds(ResultOfGetResultsIds resultOfGetResultsIds) {
        this.resultOfGetResultsIds = resultOfGetResultsIds;
    }

    public List<String> getUniprotXRefList() {
        return uniprotXRefList;
    }

    public void setUniprotXRefList(List<String> uniprotXRefList) {
        this.uniprotXRefList = uniprotXRefList;
    }


//********************************** METHODS *********************************//

}
