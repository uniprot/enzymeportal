package uk.ac.ebi.ep.restapi.exceptions;

/**
 *
 * @author joseph
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
