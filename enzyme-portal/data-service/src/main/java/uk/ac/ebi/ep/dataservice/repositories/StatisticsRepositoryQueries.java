package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author joseph
 * @param <T>
 */
@NoRepositoryBean
public interface StatisticsRepositoryQueries<T> {

    T findByReleaseId(String releaseId, Class<T> type);

    T findFirstByOrderByReleaseDateAsc(Class<T> type);

    T findTopByOrderByReleaseDateDesc(Class<T> type);

    <T> List<T> findTop12ByOrderByReleaseDateDesc(Class<T> type);

    <T> List<T> findTop3ByOrderByReleaseDateAsc(Class<T> type);

    <T> List<T> findFirst3ByOrderByReleaseDateAsc(Class<T> type);

}
