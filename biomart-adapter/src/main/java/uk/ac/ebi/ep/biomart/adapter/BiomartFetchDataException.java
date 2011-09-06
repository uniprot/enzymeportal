package uk.ac.ebi.ep.biomart.adapter;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class BiomartFetchDataException extends Exception {

    public BiomartFetchDataException(Throwable cause) {
        super(cause);
    }

    public BiomartFetchDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public BiomartFetchDataException(String message) {
        super(message);
    }

    public BiomartFetchDataException() {
    }

}
