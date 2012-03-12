/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import uk.ac.ebi.ep.exception.EnzymePortalException;
import uk.ac.ebi.ep.exception.Severity;

/**
 *this Exception class is specific to the siteMap Module of the EnzymePortal
 * @author joseph
 */
public class SiteMapException extends EnzymePortalException {

    /**
     * Creates a new SiteMapException object with the specified message and severity.
     * @param message The message to be displayed when logging this exception
     * @param severity The severity of this exception
     */
    public SiteMapException(String message, Throwable cause, Severity severity) {
        super(message, cause, severity);
    }

    /**
     * Creates a new SiteMapException object with the specified message, severity and root cause.
     * @param message The message text to be displayed when logging this exception
     * @param cause The root cause of this exception
     * @param severity The severity of this exception
     */
    public SiteMapException(String message, Severity severity) {
        super(message, severity);
    }
}
