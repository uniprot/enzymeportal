package uk.ac.ebi.ep.dataservice.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;

/**
 *
 * @author joseph
 */
@Repository
public interface EnzymePortalCofactorRepository extends JpaRepository<EnzymePortalCofactor, BigDecimal>, QuerydslPredicateExecutor<EnzymePortalCofactor>, EnzymePortalCofactorRepositoryCustom {

    @Query(value = "SELECT * FROM ENZYME_PORTAL_COFACTOR order by cofactor_name,cofactor_id", nativeQuery = true)
    List<EnzymePortalCofactor> cofactors();

    @Query(value = "SELECT c FROM EnzymePortalCofactor c order by c.cofactorName,c.cofactorId")
    Page<EnzymePortalCofactor> getPageableCofactors(Pageable pageable);
//
//    @Query(value = "SELECT * FROM ENZYME_PORTAL_COFACTOR where ROWNUM <= :limit order by cofactor_name,cofactor_id", nativeQuery = true)
//    List<EnzymePortalCofactor> cofactors(@Param("limit") int limit);
}
