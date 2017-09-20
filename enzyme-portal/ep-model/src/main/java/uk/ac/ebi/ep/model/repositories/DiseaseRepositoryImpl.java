
package uk.ac.ebi.ep.model.repositories;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.search.model.Disease;
import uk.ac.ebi.ep.model.EnzymePortalDisease;
import uk.ac.ebi.ep.model.QEnzymePortalDisease;

/**
 *
 * @author joseph
 */
public class DiseaseRepositoryImpl implements DiseaseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalDisease $ = QEnzymePortalDisease.enzymePortalDisease;

    //private final JPAQueryFactory jpaQueryFactory;
//    @Autowired
//    public DiseaseRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
//        this.jpaQueryFactory = jpaQueryFactory;
//    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnzymePortalDisease> findDiseasesByNamePrefixes(List<String> namePrefixes) {
        // JPAQuery query = new JPAQuery(entityManager);
        // JPAQueryFactory query = new JPAQueryFactory(entityManager);
        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach(prefix -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        // query.from($).where(builder);
        return jpaQueryFactory.selectFrom($).where(builder).distinct().fetch();
        // return query.distinct();

    }

    @Transactional(readOnly = true)
    @Override
    public List<EnzymePortalDisease> findDiseasesByAccessions(List<String> accessions) {
//        JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach(accession -> {

            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));

        });
       // query.from($).where(builder);

        // return query.distinct().list($);
        return query.selectFrom($).where(builder).distinct().fetch();
    }

    /**
     * Note: meshId is used as default disease id for now as some omim entries
     * are null and we don't have efo yet
     *
     * @param omimId
     *
     * @return list of accessions
     */
    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByOmimId(String omimId) {

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        List<String> entries = query.select($.uniprotAccession.accession)
                .from($)
                .where($.omimNumber.equalsIgnoreCase(omimId))
                .distinct()
                .fetch();

        return entries;

    }

    @Override
    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseases() {
        EntityGraph eGraph = entityManager.getEntityGraph("DiseaseEntityGraph");
        eGraph.addAttributeNodes("uniprotAccession");

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQuery query = new JPAQueryFactory(entityManager).query();
        //JPAQueryFactory query = new JPAQueryFactory(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);
        return query.from($).fetch();

    }

    /**
     *
     * @param taxId
     * @param name
     * @return
     * @deprecated(To be removed in version 1.0.14)
     */
//    @Deprecated
//    public List<String> findDiseasesByName(String name) {
//        JPAQuery query = new JPAQuery(entityManager);
//        return query.from($)
//                .where($.diseaseName.like(name)).distinct()
//                .list($.diseaseName);
//    }
    @Override
    @Transactional(readOnly = true)
    public List<Disease> findDiseasesByTaxId(Long taxId) {

        // EntityGraph eGraph = entityManager.getEntityGraph("DiseaseEntityGraph");
        // eGraph.addAttributeNodes("uniprotAccession");
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        // query.setHint("javax.persistence.fetchgraph", eGraph);
        List<Disease> result = query
                .select(Projections.constructor(Disease.class, $.omimNumber, $.diseaseName, $.url))
                .from($)
                .where($.uniprotAccession.taxId.eq(taxId))
                .distinct()
                .fetch();
        // .stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Disease> findDiseasesNameLike(String name) {

        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        String diseaseName = String.format("%%%s%%", name).toLowerCase();
        return query
                .select(Projections.constructor(Disease.class, $.omimNumber, $.diseaseName))
                .from($).where($.diseaseName.toLowerCase().like(diseaseName))
                .distinct()
                .fetch();

    }

    @Override
    public List<Disease> findDiseasesByAccession(String accession) {
        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        return query
                .select(Projections.constructor(Disease.class, $.omimNumber, $.diseaseName, $.definition, $.url, $.evidence))
                .from($)
                .where($.uniprotAccession.accession.equalsIgnoreCase(accession))
                .distinct()
                .fetch();

    }

    @Override
    public List<Disease> findDiseasesByEcNumber(String ecNumber) {

        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query.select(Projections.constructor(Disease.class, $.omimNumber, $.diseaseName, $.url))
                .from($)
                .where($.uniprotAccession.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber)).distinct().fetch();

        //TODO benchmark QueryDSL vs Native Query to evaluate which has faster response time and delete one later
//        String nativeQuery = "SELECT d.OMIM_NUMBER,d.DISEASE_NAME,d.URL FROM ENZYME_PORTAL_DISEASE d,ENZYME_PORTAL_EC_NUMBERS e \n"
//                + "WHERE d.UNIPROT_ACCESSION=e.UNIPROT_ACCESSION AND e.EC_NUMBER= :EC_NUMBER GROUP BY d.OMIM_NUMBER,d.DISEASE_NAME,d.URL";
//        Query query = entityManager.createNativeQuery(nativeQuery, "diseaseMapping");
//        List<Disease> result = query.setParameter("EC_NUMBER", ecNumber).getResultList();
//
//        return result.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Disease> findDiseasesInAccessions(List<String> accessions) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query.selectDistinct(Projections.constructor(Disease.class, $.omimNumber, $.diseaseName, $.url))
                .from($)
                .where($.uniprotAccession.accession.in(accessions))
                .fetch();

    }

}
