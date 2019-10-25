
package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.dataservice.dto.Disease;
import uk.ac.ebi.ep.dataservice.entities.QEnzymePortalDisease;

/**
 *
 * @author joseph
 */
 class DiseaseRepositoryImpl implements DiseaseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalDisease $ = QEnzymePortalDisease.enzymePortalDisease;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Disease> findDiseasesNameLike(String name) {

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        String diseaseName = String.format("%%%s%%", name).toLowerCase();
        return query
                .select(Projections.constructor(Disease.class, $.omimNumber, $.diseaseName))
                .from($).where($.diseaseName.toLowerCase().like(diseaseName))
                .distinct()
                .fetch();

    }

}
