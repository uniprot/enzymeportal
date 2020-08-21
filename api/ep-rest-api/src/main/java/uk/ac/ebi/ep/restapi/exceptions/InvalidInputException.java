package uk.ac.ebi.ep.restapi.exceptions;

/**
 *
 * @author joseph
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String msg) {
        super(msg);
    }
}
