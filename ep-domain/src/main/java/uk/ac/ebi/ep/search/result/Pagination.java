package uk.ac.ebi.ep.search.result;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Pagination implements Serializable {

//********************************* VARIABLES ********************************//

    protected int numberOfResults;
    protected int numberResultsPerPage;
    protected int totalPages;
    protected List<String> pageLinks;
    protected int maxDisplayedPages;
    protected int currentPage;

//******************************** CONSTRUCTORS ******************************//
    public Pagination() {
    }

    public Pagination(int numberOfResults, int numberResultsPerPage) {
        this.numberOfResults = numberOfResults;
        this.numberResultsPerPage = numberResultsPerPage;
    }


//****************************** GETTER & SETTER *****************************//


    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public int getNumberResultsPerPage() {
        return numberResultsPerPage;
    }

    public void setNumberResultsPerPage(int numberResultsPerPage) {
        this.numberResultsPerPage = numberResultsPerPage;
    }

    public List<String> getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(List<String> pageLinks) {
        this.pageLinks = pageLinks;
    }
    public int getTotalPages() {
        return totalPages;
    }
/*
    public int getTotalPages() {
        int lastPageResults = 0;
        if (numberOfResults>0 && numberResultsPerPage>0) {
            lastPageResults = getLastPageResults();
            int addPage = 0;
            if (lastPageResults > 0) {
                addPage = 1;
            }
            totalPages =
                    ((numberOfResults-lastPageResults)/numberResultsPerPage)+addPage;
        }

        return totalPages;
    }
*/
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getMaxDisplayedPages() {
        return maxDisplayedPages;
    }

    public void setMaxDisplayedPages(int maxDisplayedPages) {
        this.maxDisplayedPages = maxDisplayedPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


//********************************** METHODS *********************************//
/*
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
*/
    public int getLastPageResults() {
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

    public int calTotalPages() {
        int lastPageResults = 0;
        if (numberOfResults>0 && numberResultsPerPage>0) {
            lastPageResults = getLastPageResults();
            int addPage = 0;
            if (lastPageResults > 0) {
                addPage = 1;
            }
            totalPages =
                    ((numberOfResults-lastPageResults)/numberResultsPerPage)+addPage;
        }

        return totalPages;
    }

    public int calCurrentPage(int start) {
        if (this.numberResultsPerPage > 0) {
        currentPage =
        (start+this.numberResultsPerPage)/this.numberResultsPerPage;
        }
        return currentPage;
    }

}
