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

	private static final long serialVersionUID = 7646465324977743547L;

	protected int numberOfResults;
    /**
     * First result shown (zero-based).
     */
    protected int firstResult;
    /**
     * Last result shown (zero-based).
     */
    protected int lastResult;
    protected int resultsPerPage;
    protected List<String> pageLinks;
    protected int maxDisplayedPages;

    /**
     * Last page (pages are 1-based, as their purpose is UI).
     */
    protected int lastPage;
    
    /**
     * Current page (pages are 1-based, as their purpose is UI).
     */
    private int currentPage;

    public Pagination() {}

    public Pagination(int numberOfResults, int numberOfResultsPerPage) {
        this.numberOfResults = numberOfResults;
        this.resultsPerPage = numberOfResultsPerPage;
        recalculate();
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
        recalculate();
    }

    public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		paginate();
	}

	public int getLastResult() {
		return lastResult;
	}

	public void setLastResult(int lastResult) {
		this.lastResult = lastResult;
	}

	public int getNumberOfResultsPerPage() {
        return resultsPerPage;
    }

    public void setNumberOfResultsPerPage(int numberResultsPerPage) {
        this.resultsPerPage = numberResultsPerPage;
        recalculate();
    }

    public List<String> getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(List<String> pageLinks) {
        this.pageLinks = pageLinks;
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

    public int getLastPage() {
        return lastPage;
    }
    
    private void recalculate(){
    	if (numberOfResults == 0) firstResult = -1;
    	calculateLastPage();
    	paginate();
    }

    private void calculateLastPage(){
        lastPage = numberOfResults / resultsPerPage;
        if (numberOfResults % resultsPerPage > 0) lastPage++;
	}

	private void paginate() {
		int lastEstimatedResult = firstResult + resultsPerPage - 1;
		lastResult = lastEstimatedResult > numberOfResults - 1?
				numberOfResults - 1:
				lastEstimatedResult;
        if (resultsPerPage > 0) {
        	currentPage = (firstResult+resultsPerPage)/resultsPerPage;
        }
    }

}
