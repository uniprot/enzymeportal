package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamily;
import uk.ac.ebi.ep.dataservice.entities.QEnzymePortalFamilyName;

/**
 *
 * @author Joseph
 */
public class EnzymePortalFamilyNameRepositoryImpl implements EnzymePortalFamilyNameRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalFamilyName $ = QEnzymePortalFamilyName.enzymePortalFamilyName;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ProteinFamily> familyNameLike(String name) {

        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        String familyName = String.format("%%%s%%", name).toLowerCase();
        return query
                .select(Projections.constructor(ProteinFamily.class, $.familyGroupId, $.familyName))
                .from($)
                .where($.familyName.toLowerCase().like(familyName))
                .distinct()
                .fetch();
    }

}
