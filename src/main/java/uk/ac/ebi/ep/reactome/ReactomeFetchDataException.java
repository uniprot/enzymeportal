package uk.ac.ebi.ep.reactome;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeFetchDataException extends Exception{

    public ReactomeFetchDataException(Throwable cause) {
        super(cause);
    }

    public ReactomeFetchDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReactomeFetchDataException(String message) {
        super(message);
    }

    public ReactomeFetchDataException() {
    }

}
