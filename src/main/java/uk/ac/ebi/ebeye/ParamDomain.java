package uk.ac.ebi.ebeye;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamDomain {

//********************************* VARIABLES ********************************//
protected String domain;

    public ParamDomain(String domain) {
        this.domain = domain;
    }

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

//********************************** METHODS *********************************//

}
