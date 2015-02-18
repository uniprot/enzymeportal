package uk.ac.ebi.ep.search.exception;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class InvalidSearchException extends EnzymeFinderException{

    public InvalidSearchException() {
    }

    public InvalidSearchException(String message) {
        super(message);
    }

    public InvalidSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSearchException(Throwable cause) {
        super(cause);
    }


}
