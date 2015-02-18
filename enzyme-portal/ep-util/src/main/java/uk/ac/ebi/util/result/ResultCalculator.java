package uk.ac.ebi.util.result;

import java.util.Iterator;
import java.util.List;
//import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultCalculator {

//********************************* VARIABLES ********************************//
protected int totalResultsPerDomain;
protected int topResults;
protected int uniprotXrefResult;

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

public static int estimateTotalUniprotXrefs(
        int totalResultsPerDomain
        , int topResults
        , int uniprotXrefResults) {
    if (totalResultsPerDomain == 0 || uniprotXrefResults == 0 || topResults == 0) {
        return 0;
    }
    //quotient of Re
    int approxMultipleOfXrefs = Math.round(uniprotXrefResults/topResults);
    int approxXrefsForNextResults =
                approxMultipleOfXrefs * (totalResultsPerDomain-topResults);
    int approxXrefsForTotalResultsPerDomain =
            uniprotXrefResults + approxXrefsForNextResults;
    return approxXrefsForTotalResultsPerDomain;
}
    /**
     * Calculate the size of the results for the getResultsIds method. Normally
     * size should be the limit size per query, but if the total found
     * is smaller than the limit size then the size is equal to the total found
     * @param totalFound total found resulted from the getNumberOfResults
     * @param resultSizePerQuery the configured size of results
     * @return the size to be passed to the getResultsIds
     */
/*
    public static int calGetResultsIdsSize(int totalFound, int resultSizePerQuery) {
        int resultLimit = 0;
        if (totalFound < resultSizePerQuery) {
            resultLimit = totalFound;
        } else {
            resultLimit = resultSizePerQuery;
        }
        return resultLimit;
    }
*/
/*
    public static int calTotalResultsFound(
            List<ResultOfGetNumberOfResults> resultList) {
        if (resultList ==  null) {
            return 0;
        }
        Iterator it = resultList.iterator();
        int counter = 0;
        while (it.hasNext()) {
            ResultOfGetNumberOfResults resultOfGetNumberOfResults
                    = (ResultOfGetNumberOfResults)it.next();
            if (resultOfGetNumberOfResults != null) {
                counter = counter + resultOfGetNumberOfResults.getTotalFound();
            }            
        }
        return counter;
    }
*/
}
