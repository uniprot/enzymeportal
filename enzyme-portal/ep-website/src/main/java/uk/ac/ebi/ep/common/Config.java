package uk.ac.ebi.ep.common;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
@Service
public class Config  {

    private static final Logger LOGGER = Logger.getLogger(Config.class);


    protected int resultsPerPage = 10;
    
    protected int maxPages = 1;
  
    protected int searchCacheSize = 50;

    protected int maxMoleculesPerGroup = 3;

    /**
     * Maximum length (in words) of a text field to be shown at first instance.
     */
    public int maxTextLength = 60;
    


   
	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * This implementation understands size as the number of search terms,
	 * not their related search results.
	 */
	public void setSearchCacheSize(int size) {
		searchCacheSize = size;
	}

	public int getSearchCacheSize() {
		return searchCacheSize;
	}

	

    public int getMaxMoleculesPerGroup() {
        return maxMoleculesPerGroup;
    }

    public void setMaxMoleculesPerGroup(int maxMoleculesPerGroup) {
        this.maxMoleculesPerGroup = maxMoleculesPerGroup;
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }

    public void setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
    }
}
