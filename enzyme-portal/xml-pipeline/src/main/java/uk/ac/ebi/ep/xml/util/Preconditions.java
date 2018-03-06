
package uk.ac.ebi.ep.xml.util;

/**
 * Created by rantunes on 22/02/16.
 */
public final class Preconditions {
    private Preconditions() {}

    /**
     * 
     * @param precondition condition that must be satisfied true
     * @param message message to display if condition is true.
     */
    public static void checkArgument(boolean precondition, String message) {
        if(precondition) {
            throw new IllegalArgumentException(message);
        }
    }
}