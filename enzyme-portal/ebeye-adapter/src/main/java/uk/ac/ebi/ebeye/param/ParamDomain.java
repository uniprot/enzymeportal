package uk.ac.ebi.ebeye.param;

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
