package uk.ac.ebi.ep.adapter.ebeye.param;

import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.Domains;
import uk.ac.ebi.ep.config.Domain;

/**
 * Objects of this class wrap a query parameter:
 * <ul>
 * 	<li>domain: the {@link Domain domain} to restrict the search to.</li>
 * </ul>
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamDomain {

//********************************* VARIABLES ********************************//
protected String domain;

    public ParamDomain() {
    }

//******************************** CONSTRUCTORS ******************************//

    public ParamDomain(String domain) {
        this.domain = domain;
    }


//****************************** GETTER & SETTER *****************************//
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

//********************************** METHODS *********************************//

}
