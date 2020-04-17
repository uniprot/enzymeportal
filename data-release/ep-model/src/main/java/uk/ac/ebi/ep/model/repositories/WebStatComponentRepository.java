package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.WebStatComponent;

/**
 *
 * @author joseph
 */
public interface WebStatComponentRepository extends JpaRepository<WebStatComponent, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM web_stat_component WHERE release_id = :RELEASE_ID ", nativeQuery = true)
    WebStatComponent findByReleaseId(@Param("RELEASE_ID") String releaseId);
}
