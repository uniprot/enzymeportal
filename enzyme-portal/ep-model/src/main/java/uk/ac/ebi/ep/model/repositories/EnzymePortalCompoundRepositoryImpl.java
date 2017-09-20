
package uk.ac.ebi.ep.model.repositories;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.search.model.Compound;
import uk.ac.ebi.ep.model.EnzymePortalCompound;
import uk.ac.ebi.ep.model.QEnzymePortalCompound;

/**
 *
 * @author joseph
 */
public class EnzymePortalCompoundRepositoryImpl implements EnzymePortalCompoundRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalCompound $ = QEnzymePortalCompound.enzymePortalCompound;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public List<EnzymePortalCompound> findCompoundsByUniprotName(String uniprotName) {

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanExpression isUniprotName = idPrefix.equalsIgnoreCase(uniprotName);

        return jpaQueryFactory.selectFrom($)
                .where(isUniprotName)
                .distinct().fetch();

    }

    @Override
    public List<EnzymePortalCompound> findCompoundsByNameprefixes(List<String> namePrefixes) {

        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach(prefix -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        return jpaQueryFactory.selectFrom($)
                .where(builder).distinct().fetch();

    }

    @Override
    public List<EnzymePortalCompound> findCompoundsByUniprotAccession(String accession) {
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        BooleanExpression isUniprotAcc = $.uniprotAccession.accession.equalsIgnoreCase(accession);

        return jpaQueryFactory.selectFrom($).where(isUniprotAcc)
                .distinct()
                .fetch();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Compound> findCompoundsByTaxId(Long taxId) {

        //EntityGraph eGraph = entityManager.getEntityGraph("CompoundEntityGraph");
        //eGraph.addAttributeNodes("uniprotAccession");
        //JPAQuery query = new JPAQuery(entityManager);
        // query.setHint("javax.persistence.fetchgraph", eGraph);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .select(Projections.constructor(Compound.class, $.compoundId, $.compoundName, $.url, $.compoundRole))
                .from($)
                .where($.uniprotAccession.taxId.eq(taxId))
                .distinct().fetch();

    }

    @Override
    public List<Compound> findCompoundsByEcNumber(String ecNumber) {
        String nativeQuery = "SELECT cp.COMPOUND_ID,cp.COMPOUND_NAME,cp.URL,cp.COMPOUND_ROLE FROM ENZYME_PORTAL_COMPOUND cp, ENZYME_PORTAL_EC_NUMBERS e \n"
                + "WHERE cp.UNIPROT_ACCESSION=e.UNIPROT_ACCESSION AND e.EC_NUMBER = :EC_NUMBER GROUP BY cp.COMPOUND_ID,cp.COMPOUND_NAME,cp.URL,cp.COMPOUND_ROLE";
        //Query query = entityManager.createNativeQuery(nativeQuery, "compoundMapping");
        // List<Compound> result = query.setParameter("EC_NUMBER", ecNumber).getResultList();

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        return jpaQueryFactory
                .select(Projections.constructor(Compound.class, $.compoundId, $.compoundName, $.url, $.compoundRole))
                .from($).where($.uniprotAccession.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber))
                .distinct()
                .fetch();

    }

    @Override
    public List<Compound> findCompoundsInAccessions(List<String> accessions) {
   JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        return jpaQueryFactory
                .select(Projections.constructor(Compound.class, $.compoundId, $.compoundName, $.url, $.compoundRole))
                .from($).where($.uniprotAccession.accession.in(accessions))
                .distinct()
                .fetch();
    }

}
