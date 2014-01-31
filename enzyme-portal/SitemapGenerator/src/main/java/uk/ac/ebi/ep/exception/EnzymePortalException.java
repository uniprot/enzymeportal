/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.exception;

/**
 * The EnzymePortalException Class extends the standard Exception class, providing a
 * severity field relevant to this class use within the EnzymePortal.
 * 
 * @author Joseph
 */
public class EnzymePortalException extends Exception {
    private static final long serialVersionUID = -4582727550131460510L;

    private final Severity severity;

    /**
     * Creates a new EnzymePortalException object with the specified message and severity.
     * @param message The message text to be displayed when logging this exception
     * @param severity The severity of this exception
     */
    public EnzymePortalException(String message, Severity severity) {
        super(message);
        this.severity = severity;
    }

    /**
     * Creates a new EnzymePortalException object with the specified message, severity and root cause.
     * @param message The message text to be displayed when logging this exception
     * @param cause The root cause of this exception
     * @param severity The severity of this exception
     */
    public EnzymePortalException(String message, Throwable cause, Severity severity) {
        super(message, cause);
        this.severity = severity;
    }

    /**
     * Gets the severity of this exception. This is used to determine what action,
     * if any, should be taken to rectify the problem represented by this exception.
     * @return The severity of this exception
     */
    public Severity getSeverity() {
        return severity;
    }
}
