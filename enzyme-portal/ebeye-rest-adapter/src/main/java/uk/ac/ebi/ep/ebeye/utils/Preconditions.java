package uk.ac.ebi.ep.ebeye.utils;

/**
 * Class used to check for preconditions declared within constructors or public methods.
 *
 * @author Ricardo Antunes
 */
public final class Preconditions {
    private Preconditions() {}

    public static void checkArgument(boolean precondition, String message) {
        if(!precondition) {
            throw new IllegalArgumentException(message);
        }
    }
}
