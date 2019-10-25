package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamilyView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalFamilyName;

/**
 *
 * @author Joseph
 */
@Repository
public interface EnzymePortalFamilyNameRepository extends JpaRepository<EnzymePortalFamilyName, Long>, QuerydslPredicateExecutor<EnzymePortalFamilyName>, EnzymePortalFamilyNameRepositoryCustom {

    @Query(value = "select f.familyName as familyName, f.familyGroupId as familyGroupId from EnzymePortalFamilyName f order by f.familyName, f.familyGroupId")
    List<ProteinFamilyView> findProteinFamilies();

}
