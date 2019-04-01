package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ep.model.EnzymePortalCofactor;

/**
 *
 * @author joseph
 */
public interface EnzymePortalCofactorRepository extends JpaRepository<EnzymePortalCofactor, Long> {

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query(value = "Insert INTO ENZYME_COFACTOR (COFACTOR_INTERNAL_ID,COFACTOR_ID,COFACTOR_NAME,COFACTOR_URL) VALUES (SEQ_COFACTOR_INTERNAL_ID.NEXTVAL,?1,?2,?3) ", nativeQuery = true)
//    void createCofactor(String cofactorId, String cofactorName, String cofactorUrl);

}
