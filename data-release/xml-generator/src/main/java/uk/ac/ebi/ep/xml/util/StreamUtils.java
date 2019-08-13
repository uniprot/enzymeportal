package uk.ac.ebi.ep.xml.util;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple utility methods for manipulating collections with StreamSupport.
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class StreamUtils {

    private StreamUtils() {
    }

    public static <T> Stream<T> stream(Iterator<T> sourceIterator) {
        return compute(sourceIterator, false);
    }

    public static <T> Stream<T> parallelStream(Iterator<T> sourceIterator) {
        return compute(sourceIterator, true);
    }

    private static <T> Stream<T> compute(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    /**
     * converts an Iterable to a stream
     *
     * @param <T>
     * @param iterable
     * @return an ordered stream of the Iterable
     */
    public static <T> Stream<T> streamOrdered(final Iterable<T> iterable) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        iterable.iterator(),
                        Spliterator.ORDERED
                ),
                false
        );
    }

    /**
     * converts Iterable to a List
     *
     * @param <T>
     * @param iterable
     * @return the List
     */
    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
