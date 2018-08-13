package uk.ac.ebi.ep.model.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.hibernate.ScrollableResults;

/**
 * utility to stream ScrollableResults
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public final class QueryStreamUtil {

    private QueryStreamUtil() {
    }

    public static <T> Stream<T> resultStream(Class<T> clazz, int batchSize, ScrollableResults query) {
        return resultStream(new ScrollableResultsSpliterator<T>(clazz, batchSize, query));
    }

    public static <T> Stream<T> resultStream(ScrollableResultsSpliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false)
                .onClose(spliterator::close);
    }
}
