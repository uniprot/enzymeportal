package uk.ac.ebi.ebeye.param;

import uk.ac.ebi.ep.config.Domain;

/**
 * Objects of this class wrap query parameters:
 * <ul>
 * 	<li>domain: the {@link Domain domain} to restrict the search to.</li>
 * 	<li>query: a Lucene query.</li>
 * 	<li>totalFound: the total number of results found.</li>
 * </ul>
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamGetNumberOfResults extends ParamDomain{

//********************************* VARIABLES ********************************//
protected String query;
protected int totalFound;

    public ParamGetNumberOfResults(String domain, String query) {
        super(domain);
        this.query = query;
    }

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

    


//********************************** METHODS *********************************//

}
