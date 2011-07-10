package uk.ac.ebi.ebeye.util;

import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 @deprecated  See Pagination
 */
public class Calculator {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//


   public static int getLastPageResults(int numberOfResults, int numberResultsPerPage) {
        int lastPageRecords = 0;
        if (numberOfResults>0 && numberResultsPerPage>0) {
            if (numberOfResults>numberResultsPerPage) {
                lastPageRecords = (numberOfResults%numberResultsPerPage);
            }
            else {
                lastPageRecords = numberOfResults;
            }
        }
        return lastPageRecords;
    }

    public static int calTotalPages(int numberOfResults, int numberResultsPerPage) {
        int lastPageResults = 0;
        int totalPages = 0;
        if (numberOfResults>0 && numberResultsPerPage>0) {
            lastPageResults = getLastPageResults(
                    numberOfResults,numberResultsPerPage);
            int addPage = 0;
            if (lastPageResults > 0) {
                addPage = 1;
            }
            totalPages =
                    ((numberOfResults-lastPageResults)/numberResultsPerPage)+addPage;
        }

        return totalPages;
    }


}