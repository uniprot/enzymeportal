package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.dataservice.dto.Pathway;
import uk.ac.ebi.ep.dataservice.entities.QEnzymePortalPathways;

/**
 *
 * @author joseph
 */
public class EnzymePortalPathwaysRepositoryImpl implements EnzymePortalPathwaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalPathways $ = QEnzymePortalPathways.enzymePortalPathways;

    @Transactional(readOnly = true)
    @Override
    public List<Pathway> findPathwaysWithNamesLike(String name) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        String pathwayName = String.format("%%%s%%", name).toLowerCase();
          return jpaQueryFactory
                .select(Projections.constructor(Pathway.class, $.pathwayGroupId, $.pathwayId, $.pathwayName))
                .from($)
                .where($.pathwayName.toLowerCase().like(pathwayName))
                .fetch();

    }
}
