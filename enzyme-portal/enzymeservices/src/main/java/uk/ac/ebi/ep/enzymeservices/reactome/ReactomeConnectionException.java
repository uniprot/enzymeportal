package uk.ac.ebi.ep.enzymeservices.reactome;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ReactomeConnectionException extends ReactomeServiceException{

    public ReactomeConnectionException() {
    }

    public ReactomeConnectionException(String message) {
        super(message);
    }

    public ReactomeConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReactomeConnectionException(Throwable cause) {
        super(cause);
    }

}
