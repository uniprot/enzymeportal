package uk.ac.ebi.ep.enzymeservices.reactome;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeFetchDataException extends ReactomeServiceException{

    public ReactomeFetchDataException() {
    }

    public ReactomeFetchDataException(String message) {
        super(message);
    }

    public ReactomeFetchDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReactomeFetchDataException(Throwable cause) {
        super(cause);
    }

}
