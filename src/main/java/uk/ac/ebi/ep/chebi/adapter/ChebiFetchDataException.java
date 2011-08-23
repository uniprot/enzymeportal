package uk.ac.ebi.ep.chebi.adapter;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ChebiFetchDataException extends Exception {

    public ChebiFetchDataException(Throwable cause) {
        super(cause);
    }

    public ChebiFetchDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChebiFetchDataException(String message) {
        super(message);
    }

    public ChebiFetchDataException() {
    }

}
