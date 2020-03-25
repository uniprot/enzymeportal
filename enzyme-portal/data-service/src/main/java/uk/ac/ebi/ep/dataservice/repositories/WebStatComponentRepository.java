package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import uk.ac.ebi.ep.dataservice.dto.WebComponentDto;
import uk.ac.ebi.ep.dataservice.entities.WebStatComponent;

/**
 *
 * @author joseph
 */
public interface WebStatComponentRepository extends StatisticsRepositoryQueries<WebComponentDto>, JpaRepository<WebStatComponent, Long>, JpaSpecificationExecutor, QuerydslPredicateExecutor<WebStatComponent> {

    @Query(value = "select release_id from WEB_STAT_COMPONENT order by release_date asc", nativeQuery = true)
    List<String> findReleaseIds();
}
