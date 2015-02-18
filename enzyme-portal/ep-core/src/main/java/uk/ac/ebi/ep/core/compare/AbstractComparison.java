package uk.ac.ebi.ep.core.compare;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Partial implementation of the {@link Comparison} interface.
 * 
 * @author rafa
 * @since 1.1.0
 * @param <T> the type to be compared.
 */
public abstract class AbstractComparison<T> implements Comparison<T> {

    /** Any underlying comparisons to this one. */
    protected Map<String, Comparison<?>> subComparisons =
            new LinkedHashMap<String, Comparison<?>>();

    /** Stored flag for any difference. */
    protected boolean differ;

    /** The compared items. */
    protected T[] compared;

    public Map<String, Comparison<?>> getSubComparisons() {
        return subComparisons;
    }

    public boolean isDifferent() {
        return differ;
    }

    public T[] getCompared() {
        return compared;
    }

    /**
     * Initialises any sub-comparisons and the <code>differ</code> boolean flag,
     * in a null-safe way.
     */
    protected void init(T t1, T t2) {
        if (t1 != null && t2 != null){
            getSubComparisons(t1, t2);
            for (Comparison<?> subComparison : subComparisons.values()) {
                differ = differ || subComparison.isDifferent();
            }
        } else if (t1 == null ^ t2 == null){
            differ = true;
        }
    }
    
    /**
     * Prepares any sub-comparisons needed, and updates the <code>differ<code>
     * flag accordingly.
     * @param t1 one object to be compared.
     * @param t2 the other object to be compared.
     */
    protected abstract void getSubComparisons(T t1, T t2);
    
}
