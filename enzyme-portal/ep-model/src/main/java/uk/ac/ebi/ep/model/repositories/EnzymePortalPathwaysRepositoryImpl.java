package uk.ac.ebi.ep.model.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.search.model.Pathway;
import uk.ac.ebi.ep.model.QEnzymePortalPathways;

/**
 *
 * @author joseph
 */
public class EnzymePortalPathwaysRepositoryImpl implements EnzymePortalPathwaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalPathways $ = QEnzymePortalPathways.enzymePortalPathways;

    @Transactional(readOnly = true)
    // @Override
    public List<Pathway> findPathwaysByAccession(String accession) {

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        List<Pathway> pathways = jpaQueryFactory
                .select(Projections.constructor(Pathway.class, $.pathwayId, $.pathwayName))
                .from($)
                .where($.uniprotAccession.accession.equalsIgnoreCase(accession))
                .fetch();

        return pathways;
    }

//    @Transactional(readOnly = true)
//    @Override
//    public List<String> findAccessionsByPathwayId(String pathwayId) {
//
//        List<String> enzymes = new ArrayList<>();
//        JPAQuery query = new JPAQuery(entityManager);
//
//        List<EnzymePortalPathways> entries = query.from($).where($.pathwayId.equalsIgnoreCase(pathwayId)).distinct().list($)
//                .stream().distinct().collect(Collectors.toList());
//
//        entries.stream().forEach(e -> {
//            enzymes.add(e.getUniprotAccession().getAccession());
//        });
//
//        return enzymes;
//
//    }
    @Transactional(readOnly = true)
    @Override
    public List<Pathway> findPathwaysByName(String name) {
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        String pathwayName = String.format("%%%s%%", name).toLowerCase();
        return jpaQueryFactory
                .select(Projections.constructor(Pathway.class, $.pathwayGroupId, $.pathwayId, $.pathwayName))
                .from($)
                .where($.pathwayName.toLowerCase().like(pathwayName))
                .distinct()
                .fetch();

    }
}
