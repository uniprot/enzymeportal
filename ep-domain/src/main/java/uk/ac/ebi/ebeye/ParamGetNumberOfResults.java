package uk.ac.ebi.ebeye;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author: hongcao
 * @deprecated 
 */
public class ParamGetNumberOfResults extends ParamDomain{

//********************************* VARIABLES ********************************//
protected String query;

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


//********************************** METHODS *********************************//

}
