
package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.model.UniprotXref;

/**
 *
 * @author joseph
 */
public interface UniprotXrefRepository extends JpaRepository<UniprotXref, Long>, QueryDslPredicateExecutor<UniprotXref>, UniprotXrefRepositoryCustom {

}
