package uk.ac.ebi.ep.search.exception;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class MultiThreadingException extends EnzymeFinderException {


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//

    public MultiThreadingException() {
    }

    public MultiThreadingException(String message) {
        super(message);
    }

    public MultiThreadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultiThreadingException(Throwable cause) {
        super(cause);
    }

//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

}
