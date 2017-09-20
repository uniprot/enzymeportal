package uk.ac.ebi.ep.model.repositories;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.search.model.EcNumber;
import uk.ac.ebi.ep.model.QEnzymePortalEcNumbers;

/**
 *
 * @author joseph
 */
public class EnzymePortalEcNumbersRepositoryImpl implements EnzymePortalEcNumbersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalEcNumbers $ = QEnzymePortalEcNumbers.enzymePortalEcNumbers;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByEc(String ecNumber) {
        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectDistinct($.uniprotAccession.accession)
                .from($)
                .where($.ecNumber.equalsIgnoreCase(ecNumber))
                .distinct()
                .fetch();

    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByEc(String ecNumber, int limit) {
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectDistinct($.uniprotAccession.accession)
                .from($)
                .where($.ecNumber.equalsIgnoreCase(ecNumber))
                .distinct()
                .limit(limit)
                .fetch();

    }

    @Transactional(readOnly = true)
    @Override
    public List<EcNumber> findEnzymeFamiliesByTaxId(Long taxId) {

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        List<EcNumber> result = jpaQueryFactory
                .select(Projections.constructor(EcNumber.class, $.ecFamily))
                .from($)
                .where($.uniprotAccession.taxId.eq(taxId))
                .distinct()
                .fetch();

        result.sort(SORT_BY_EC);
        return result;
    }
    public static final Comparator<EcNumber> SORT_BY_EC = (EcNumber ec1, EcNumber ec2) -> ec1.getEc().compareTo(ec2.getEc());

    @Override
    public List<EcNumber> findEnzymeFamiliesByEcNumber(String ecNumber) {
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        List<EcNumber> result = jpaQueryFactory
                .select(Projections.constructor(EcNumber.class, $.ecFamily))
                .from($)
                .where($.ecNumber.eq(ecNumber)).distinct().fetch();

        result.sort(SORT_BY_EC);
        return result;
    }

    @Override
    public List<EcNumber> findEcNumberInAccessions(List<String> accessions) {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        return jpaQueryFactory
                .select(Projections.constructor(EcNumber.class, $.ecFamily))
                .from($)
                .where($.uniprotAccession.accession.in(accessions))
                .distinct()
                .fetch();
    }
}
