package uk.ac.ebi.ep.dataservice.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.dataservice.dto.Taxonomy;
import uk.ac.ebi.ep.dataservice.entities.QEnzymePortalTaxonomy;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymePortalTaxonomyRepositoryImpl implements EnzymePortalTaxonomyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalTaxonomy $ = QEnzymePortalTaxonomy.enzymePortalTaxonomy;

    @Transactional(readOnly = true)
    @Override
    public List<Taxonomy> getCountForOrganisms(List<Long> taxids) {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName, $.taxId.count()))
                .from($)
                .where($.taxId.in(taxids))
                .distinct()
                .groupBy($.taxId, $.scientificName, $.commonName)
                .fetch();

    }

    @Override
    public List<Taxonomy> getModelOrganisms() {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName, $.numberOfProteins))
                .from($)
                .where($.modelOrganism.eq(BigInteger.ONE))
                .distinct()
                .groupBy($.taxId, $.scientificName, $.commonName, $.numberOfProteins)
                .fetch();

    }

    @Override
    public List<Taxonomy> organismNameLike(String name) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        String organismName = String.format("%s%%", name).toLowerCase();

        List<Taxonomy> result = exactNameMatch(jpaQueryFactory, name);

        if (result.isEmpty()) {
            result = scientificNameSearch(jpaQueryFactory, organismName);

            if (result.isEmpty()) {
                result = commonNameSearch(jpaQueryFactory, organismName);

                if (result.isEmpty()) {
                    result = containsName(jpaQueryFactory, name);

                }
            }

        }

        return result;

    }

    private List<Taxonomy> scientificNameSearch(JPAQueryFactory jpaQueryFactory, String organismName) {
        return jpaQueryFactory
                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName))
                .from($)
                .where($.scientificName.toLowerCase().like(organismName))
                .distinct()
                .fetch();
    }

    private List<Taxonomy> commonNameSearch(JPAQueryFactory jpaQueryFactory, String organismName) {
        return jpaQueryFactory
                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName))
                .from($)
                .where($.commonName.toLowerCase().like(organismName))
                .distinct()
                .fetch();
    }

    private List<Taxonomy> exactNameMatch(JPAQueryFactory jpaQueryFactory, String name) {

        return jpaQueryFactory
                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName))
                .from($)
                .where($.scientificName.toLowerCase().like(name.toLowerCase())
                        .or($.commonName.toLowerCase().like(name.toLowerCase())))
                .distinct()
                .fetch();
    }

    private List<Taxonomy> containsName(JPAQueryFactory jpaQueryFactory, String name) {
        return jpaQueryFactory
                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName))
                .from($)
                .where($.scientificName.toLowerCase().contains(name.toLowerCase()))
                .distinct()
                .fetch();
    }

}
