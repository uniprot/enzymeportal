package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;
import uk.ac.ebi.ep.dataservice.entities.QEnzymePortalCofactor;

/**
 *
 * @author joseph
 */
public class EnzymePortalCofactorRepositoryImpl implements EnzymePortalCofactorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalCofactor $ = QEnzymePortalCofactor.enzymePortalCofactor;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Cofactor> cofactorsNameLike(String name) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        String cofactorName = String.format("%%%s%%", name).toLowerCase();
        return query
                .select(Projections.constructor(Cofactor.class, $.cofactorId, $.cofactorName))
                .from($)
                .where($.cofactorName.toLowerCase().like(cofactorName))
                .distinct()
                .fetch();
    }
}
