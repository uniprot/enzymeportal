package uk.ac.ebi.ep.data.exceptions;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeRetrieverException extends Exception{

    public EnzymeRetrieverException(Throwable cause) {
        super(cause);
    }

    public EnzymeRetrieverException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnzymeRetrieverException(String message) {
        super(message);
    }

    public EnzymeRetrieverException() {
    }


}
