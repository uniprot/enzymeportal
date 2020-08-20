package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalMetabolite;

/**
 *
 * @author joseph
 */
public interface EnzymePortalMetaboliteRepository extends JpaRepository<EnzymePortalMetabolite, Long>, QuerydslPredicateExecutor<EnzymePortalMetabolite>, EnzymePortalMetaboliteRepositoryCustom {

    @Query(value = "select m.metaboliteId as metaboliteId, m.metaboliteName as metaboliteName from EnzymePortalMetabolite m group by m.metaboliteId, m.metaboliteName")
    List<MetaboliteView> findMetabolites();

    @Query(value = "select m.metaboliteId as metaboliteId, m.metaboliteName as metaboliteName from EnzymePortalMetabolite m group by m.metaboliteId, m.metaboliteName")
    Page<MetaboliteView> getPageableMetaboliteView(Pageable pageable);

//    @Query(value = "select metabolite_id as metaboliteId, metabolite_name as metaboliteName from enzyme_portal_metabolite where ROWNUM <= :limit group by metabolite_id, metabolite_name",nativeQuery = true)
//   // @Query(value = "select m.metaboliteId as metaboliteId, m.metaboliteName as metaboliteName from EnzymePortalMetabolite where ROWNUM <= :limit m group by m.metaboliteId, m.metaboliteName",nativeQuery = true)
//    List<MetaboliteView> findMetabolites(@Param("limit") int limit);
    @Query(value = "SELECT * FROM ENZYME_PORTAL_METABOLITE order by metabolite_name,metabolite_id", nativeQuery = true)
    List<EnzymePortalMetabolite> metabolites();
//
//    @Query(value = "SELECT * FROM ENZYME_PORTAL_METABOLITE where ROWNUM <= :limit order by metabolite_name,metabolite_id", nativeQuery = true)
//    List<EnzymePortalMetabolite> metabolites(@Param("limit") int limit);
}
