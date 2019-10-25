package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalTaxonomy;


/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface EnzymePortalTaxonomyRepository extends JpaRepository<EnzymePortalTaxonomy, Long>, QuerydslPredicateExecutor<EnzymePortalTaxonomy>, EnzymePortalTaxonomyRepositoryCustom {

    @Query(value = "SELECT * FROM ENZYME_PORTAL_TAXONOMY WHERE model_organism = 1", nativeQuery = true)
    List<EnzymePortalTaxonomy> findModelOrganisms();
}
