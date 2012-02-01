package uk.ac.ebi.ep.adapter.chebi;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ChebiFetchDataException extends Exception {

	private static final long serialVersionUID = 1L;

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
