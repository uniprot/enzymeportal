package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.ProteinGroups;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface ProteinGroupsRepository extends JpaRepository<ProteinGroups, Long>, QueryDslPredicateExecutor<ProteinGroups>, DataStreamingService {

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ COUNT(*) FROM PROTEIN_GROUPS", nativeQuery = true)
    Long countProteinGroups();

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM PROTEIN_GROUPS", nativeQuery = true)
    List<ProteinGroups> findProteinGroups();
}
