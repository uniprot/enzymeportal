package uk.ac.ebi.ep.core.compare;

import java.util.List;

/**
 * Basic comparison of objects to state if they differ or not.
 * @author rafa
 * @since 1.1.0
 *
 */
public interface Comparison<T> {

    /**
     * Do these objects differ?
     * @return <code>true</code> if there is something different.
     */
    public boolean differ();

    /**
     * Getter for the compared items.
     * @return the compared items.
     */
    public T[] getCompared();

    /**
     * Retrieves the underlying comparisons made in order to know if the objects
     * differ.
     * @return A list of comparisons.
     */
    public List<Comparison<?>> getSubComparisons();
}
