package uk.ac.ebi.ep.core.search;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Pagination {
    protected int lastPageRecords;
    protected int totalPages;

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//

    public int getLastPageRecords() {
        return lastPageRecords;
    }

    public void setLastPageRecords(int lastPageRecords) {
        this.lastPageRecords = lastPageRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


//********************************** METHODS *********************************//

    public void paginateResults(int numberOfResults, int numberResultPerPage) {
        if (numberOfResults < numberResultPerPage) {
            if (numberOfResults==0) {
                lastPageRecords = 0;
                totalPages=0;
            }
            else {
                lastPageRecords = numberOfResults;
                totalPages=1;
            }
        }
        else {
            lastPageRecords = (numberOfResults%numberResultPerPage);
            if (lastPageRecords > 0) {
                totalPages = ((numberOfResults-lastPageRecords)/numberResultPerPage)+1;
            }
            else {
                totalPages = numberOfResults/numberResultPerPage;
            }
        }
        

    }

}
