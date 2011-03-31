package uk.ac.ebi.ep.core.search;

import javax.xml.rpc.ServiceException;
import uk.ac.ebi.webservices.jaxws.EBeyeClient;
import uk.ac.ebi.webservices.jaxws.stubs.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EBeyeRequestProcessor extends EBeyeClient {

//********************************* VARIABLES ********************************//
    protected String domainId;
    protected String query;
    protected String[] retVal;

//******************************** CONSTRUCTORS ******************************//
//****************************** GETTER & SETTER *****************************//
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getRetVal() {
        return retVal;
    }

    public void setRetVal(String[] retVal) {
        this.retVal = retVal;
    }

//********************************** METHODS *********************************//
    public void getAllResultsIds() throws ServiceException {
        ArrayOfString result = this.getSrvProxy().getAllResultsIds(domainId, query);
        retVal = result.getString().toArray(new String[0]);

        for (int i = 0; i < retVal.length; i++) {
            System.out.println(retVal[i]);
        }
        System.out.println(retVal.length);
    }
}
