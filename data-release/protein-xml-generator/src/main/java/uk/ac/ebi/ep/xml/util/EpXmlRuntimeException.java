
package uk.ac.ebi.ep.xml.util;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EpXmlRuntimeException extends RuntimeException {

    public EpXmlRuntimeException(String message) {
        super(message);
    }

    public EpXmlRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
