package uk.ac.ebi.ep.core.compare;

import java.util.ArrayList;
import java.util.List;

/**
 * Partial implementation of the {@link Comparison} interface.
 * 
 * @author rafa
 * @since 1.1.0
 * @param <T> the type to be compared.
 */
public abstract class AbstractComparison<T> implements Comparison<T> {

    /** Any underlying comparisons to this one. */
    protected List<Comparison<?>> subComparisons =
            new ArrayList<Comparison<?>>();

    /** Stored flag for any difference. */
    protected boolean differ;

    /** The compared items. */
    protected T[] compared;

    /**
     * Calculates the value of the <code>differ</code> boolean flag according to
     * any underlying sub-comparisons.
     */
    protected void doDiffer() {
        for (Comparison<?> subComparison : subComparisons) {
            differ = differ || subComparison.isDifferent();
        }
    }

    public List<Comparison<?>> getSubComparisons() {
        return subComparisons;
    }

    public boolean isDifferent() {
        return differ;
    }

    public T[] getCompared() {
        return compared;
    }

}
