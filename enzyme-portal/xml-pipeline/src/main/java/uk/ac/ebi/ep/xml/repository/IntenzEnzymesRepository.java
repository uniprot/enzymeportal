package uk.ac.ebi.ep.xml.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.xml.entity.IntenzEnzymes;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface IntenzEnzymesRepository extends JpaRepository<IntenzEnzymes, Long> {

    @Query(value = "SELECT * FROM INTENZ_ENZYMES WHERE TRANSFER_FLAG = 'N' AND ACC_PRESENT='N' ORDER BY EC_NUMBER", nativeQuery = true)
    List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc();

    @Query(value = "SELECT COUNT(DISTINCT EC_NUMBER) FROM INTENZ_ENZYMES", nativeQuery = true)
    Long countIntenzEnzymes();

    ///demo
    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM INTENZ_ENZYMES WHERE TRANSFER_FLAG = 'N' AND ACC_PRESENT='N' AND ROWNUM<=:LIMIT ORDER BY EC_NUMBER", nativeQuery = true)
    List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc(@Param("LIMIT") Integer limit);

}
