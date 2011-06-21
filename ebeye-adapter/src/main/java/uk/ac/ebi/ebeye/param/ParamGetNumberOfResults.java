package uk.ac.ebi.ebeye.param;

/**
 *
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
