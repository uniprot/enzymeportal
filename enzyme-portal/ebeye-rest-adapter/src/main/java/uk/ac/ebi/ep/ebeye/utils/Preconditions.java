package uk.ac.ebi.ep.ebeye.utils;

/**
 * Created by rantunes on 22/02/16.
 */
public final class Preconditions {
    private Preconditions() {}

    public static void checkArgument(boolean precondition, String message) {
        if(!precondition) {
            throw new IllegalArgumentException(message);
        }
    }
}
