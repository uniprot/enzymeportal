package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
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

    @Query(value = "SELECT * FROM ENZYME_PORTAL_METABOLITE order by metabolite_name,metabolite_id", nativeQuery = true)
    List<EnzymePortalMetabolite> metabolites();
}
