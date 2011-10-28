package uk.ac.ebi.ep.adapter.intenz.exception;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class IntenzException extends Exception{

    public IntenzException(Throwable cause) {
        super(cause);
    }

    public IntenzException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntenzException(String message) {
        super(message);
    }

    public IntenzException() {
    }

}
