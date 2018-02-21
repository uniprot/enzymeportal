package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.model.IntenzEnzymes;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface IntenzEnzymesRepository extends JpaRepository<IntenzEnzymes, Long>, QueryDslPredicateExecutor<IntenzEnzymes>, DataStreamingService {

    //@Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ *  FROM INTENZ_ENZYMES", nativeQuery = true)
    List<IntenzEnzymes> findIntenzEnzymes();

    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM INTENZ_ENZYMES WHERE TRANSFER_FLAG = 'N' AND ACC_PRESENT='N' AND ROWNUM<=:LIMIT ORDER BY EC_NUMBER", nativeQuery = true)
    List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc(@Param("LIMIT") Integer limit);

    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM INTENZ_ENZYMES WHERE TRANSFER_FLAG = 'N' AND ACC_PRESENT='N' ORDER BY EC_NUMBER", nativeQuery = true)
    List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc();

    @Query(value = "SELECT * FROM INTENZ_ENZYMES WHERE TRANSFER_FLAG = 'N' ORDER BY EC_NUMBER", nativeQuery = true)
    List<IntenzEnzymes> findNonTransferredEnzymes();

    @Query(value = "SELECT * FROM INTENZ_ENZYMES WHERE EC_NUMBER = :EC_NUMBER AND TRANSFER_FLAG = 'N'", nativeQuery = true)
    IntenzEnzymes findByEcNumber(@Param("EC_NUMBER") String ecNumber);
}
