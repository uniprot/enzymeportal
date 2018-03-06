/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
