package uk.ac.ebi.ep.data.repositories;

import java.util.stream.Stream;
import org.hibernate.ScrollMode;
import org.hibernate.SessionFactory;
import uk.ac.ebi.ep.data.util.QueryStreamUtil;

/**
 * Service to stream data from a data store using Hibernate SessionFactory
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface DataStreamingService {

    default <T> Stream<T> streamingService(Class<T> clazz, SessionFactory sessionFactory, String query, int batchSize) {

        return QueryStreamUtil.resultStream(clazz, batchSize, sessionFactory
                .openSession()
                .createQuery(query).scroll(ScrollMode.FORWARD_ONLY));

    }
}
