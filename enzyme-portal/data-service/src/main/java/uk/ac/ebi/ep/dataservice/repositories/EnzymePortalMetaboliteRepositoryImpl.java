package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.dataservice.dto.Metabolite;
import uk.ac.ebi.ep.dataservice.entities.QEnzymePortalMetabolite;

/**
 *
 * @author joseph
 */
public class EnzymePortalMetaboliteRepositoryImpl implements EnzymePortalMetaboliteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalMetabolite $ = QEnzymePortalMetabolite.enzymePortalMetabolite;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Metabolite> findMetaboliteNameLike(String name) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        String metaboliteName = String.format("%%%s%%", name).toLowerCase();
        return query
                .select(Projections.constructor(Metabolite.class, $.metaboliteId, $.metaboliteName))
                .from($).where($.metaboliteName.toLowerCase().like(metaboliteName))
                .distinct()
                .fetch();
    }
}
