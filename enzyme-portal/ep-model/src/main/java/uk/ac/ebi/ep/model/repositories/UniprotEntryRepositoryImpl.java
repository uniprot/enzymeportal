package uk.ac.ebi.ep.model.repositories;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ebi.ep.model.QUniprotEntry;
import uk.ac.ebi.ep.model.UniprotEntry;

/**
 *
 * @author joseph
 */
public class UniprotEntryRepositoryImpl implements UniprotEntryRepositoryCustom {

    //@Autowired
    //private SessionFactory sessionFactory;
    @PersistenceContext
    //@PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    private static final QUniprotEntry $ = QUniprotEntry.uniprotEntry;

//    private final JPAQueryFactory jpaQueryFactory;
//
//    @Autowired
//    public UniprotEntryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
//        this.jpaQueryFactory = jpaQueryFactory;
//    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes) {
        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "enzymePortalEcNumbersSet");

//        JPAQuery query = new JPAQuery(entityManager);
//
//        query.setHint("javax.persistence.fetchgraph", eGraph);
//        query.setHint("javax.persistence.query.timeout", 450);
        //StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        StringExpression idPrefix = $.relatedProteinsId.namePrefix;
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach(prefix -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
       // query.from($).where(builder);

        //return query.distinct().list($);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectFrom($).where(builder).distinct().fetch();
    }

//    @Deprecated
//    @Transactional(readOnly = true)
//    @Override
//    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {
//
//        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");
//
//        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
//                "uniprotXrefSet", "enzymePortalEcNumbersSet");
//        //JPAQuery query = new JPAQuery(entityManager);
//
//        // query.setHint("javax.persistence.fetchgraph", eGraph);
//        BooleanBuilder builder = new BooleanBuilder();
//        accessions.stream().forEach(accession -> {
//
//            builder.or($.accession.equalsIgnoreCase(accession));
//
//        });
//        //  query.from($).where(builder);
//        // return query.distinct().list($).stream().distinct().collect(Collectors.toList());
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory.selectFrom($).where(builder).distinct().fetch();
//    }
//    @Transactional(readOnly = true)
//    @Override
//    public List<UniprotEntry> findEnzymeByNamePrefix(String namePrefix) {
//
//        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");
//
//        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
//                "uniprotXrefSet", "enzymePortalEcNumbersSet");
//        JPAQuery query = new JPAQuery(entityManager);
//        query.setHint("javax.persistence.fetchgraph", eGraph);
//
//        //StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
//        StringExpression idPrefix = $.relatedProteinsId.namePrefix;
//        BooleanBuilder builder = new BooleanBuilder();
//
//        builder.or(idPrefix.equalsIgnoreCase(namePrefix));
//
//        //query.from($).where(builder);
//        // return query.list($).stream().distinct().collect(Collectors.toList());
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory.selectFrom($).where(builder).distinct().fetch();
//
//    }
//    @Override
//    public List<UniprotEntry> findEnzymesByAccession(String accession) {
//        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");
//
//        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
//                "uniprotXrefSet", "enzymePortalEcNumbersSet");
//       // JPAQuery query = new JPAQuery(entityManager);
//
//        // query.setHint("javax.persistence.fetchgraph", eGraph);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .selectDistinct($)
//                .where($.accession.equalsIgnoreCase(accession)).fetch();
//
//    }
//    @Transactional(readOnly = true)
//    @Override
//    public List<Taxonomy> getCountForOrganisms(List<Long> taxids) {
//
//        //JPAQuery query = new JPAQuery(entityManager);
//        // List<Tuple> tuple =    query.from($).groupBy($.taxId,$.scientificName,$.commonName).orderBy($.taxId.count().desc()).limit(11).list($.taxId,$.scientificName,$.commonName,$.taxId.count());
////select distinct uniprotEntry.tax_Id, uniprotEntry.scientific_Name, uniprotEntry.common_Name, count(uniprotEntry.tax_Id) as numEnzymes from UNIPROT_ENTRY uniprotEntry where uniprotEntry.tax_Id in ('9606') group by uniprotEntry.tax_Id, uniprotEntry.scientific_Name, uniprotEntry.common_Name;
////select distinct uniprotEntry.tax_Id, count(uniprotEntry.tax_Id) as numEnzymes from ENZYMES_TO_TAXONOMY uniprotEntry where uniprotEntry.tax_Id in ('9606') group by uniprotEntry.tax_Id;
//JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .select(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName, $.taxId.count()))
//                .from($)
//                .where($.taxId.in(taxids)).distinct().groupBy($.taxId, $.scientificName, $.commonName)
//                .fetch();
//
////                List<Taxonomy> result = query.from($).where($.taxId.in(taxids)).distinct().groupBy($.taxId, $.scientificName, $.commonName).
////                list(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName));
//// 
////        String nativeQuery = "select distinct uniprotEntry.tax_Id, uniprotEntry.scientific_Name, uniprotEntry.common_Name, count(uniprotEntry.tax_Id) as numEnzymes from UNIPROT_ENTRY uniprotEntry where uniprotEntry.tax_Id in (:TAX_ID) group by uniprotEntry.tax_Id, uniprotEntry.scientific_Name, uniprotEntry.common_Name";
////        Query query = entityManager.createNativeQuery(nativeQuery, "browseTaxonomy");
////        List<Taxonomy> result = query.setParameter("TAX_ID", taxids).getResultList();
//        //return result;
//    }
    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByTaxId(Long taxId) {
        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectDistinct($.accession)
                .from($)
                .where($.taxId.eq(taxId)).fetch();
        //return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByTaxId(Long taxId) {
        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectFrom($)
                .from($)
                .where($.taxId.eq(taxId)).distinct().fetch();

    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Page<EnzymeSummary> findEnzymesByAccessions(List<String> accessions, Pageable pageable) {
//        //JPAQuery query = new JPAQuery(entityManager);
//        //CollQueryFactory.
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        List<EnzymeSummary> result = jpaQueryFactory
//                .select(Projections.constructor(EnzymeSummary.class, $))
//                .from($)
//                .where($.accession.in(accessions))
//                .fetch();
//
//        return new PageImpl<>(result, pageable, result.size());
//
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Species> findSpeciesByTaxId(Long taxId) {
//        //JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .select(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId))
//                .from($)
//                .where($.taxId.eq(taxId))
//                .distinct()
//                .fetch();
//
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Species> findSpeciesByScientificName(String sName) {
//        //JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .select(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId))
//                .from($)
//                .where($.scientificName.equalsIgnoreCase(sName))
//                .distinct()
//                .fetch();
//
//    }

    @Override
    public List<UniprotEntry> findEnzymesByMeshId(String meshId) {
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory
                .selectFrom($)
                .where($.enzymePortalDiseaseSet.any().omimNumber.trim().equalsIgnoreCase(meshId))
                .distinct()
                .fetch();

    }

    @Override
    public List<UniprotEntry> findEnzymesByPathwayId(String pathwayId) {
        // JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectFrom($)
                .where($.enzymePortalPathwaysSet.any().pathwayId.trim().equalsIgnoreCase(pathwayId))
                .distinct()
                .fetch();

    }

    @Override
    public List<String> findEnzymesByCompound(String compoundId) {
        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectDistinct($.accession)
                .from($)
                .where($.enzymePortalCompoundSet.any().compoundId.trim().equalsIgnoreCase(compoundId))
                .fetch();

    }

    @Override
    public List<UniprotEntry> findSummariesByAcc(List<String> accessions) {

        //EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");
        // JPAQuery query = new JPAQuery(entityManager);
        //query.setHint("javax.persistence.loadgraph", eGraph);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectFrom($)
                .where($.accession.in(accessions))
                .distinct()
                .fetch();

    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Protein> findProteinByEc(String ec) {
//        // JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory.select(Projections.constructor(Protein.class, $.accession, $.proteinName))
//                .from($)
//                .where($.enzymePortalEcNumbersSet.any().ecNumber.equalsIgnoreCase(ec))
//                .distinct()
//                .fetch();
//
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Protein> findProteinByEc(String ec, int limit) {
//        // JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory.select(Projections.constructor(Protein.class, $.accession, $.proteinName, $.commonName))
//                .from($)
//                .where($.enzymePortalEcNumbersSet.any().ecNumber.equalsIgnoreCase(ec).and($.entryType.eq(Short.valueOf("0"))))
//                .distinct()
//                .limit(limit)
//                .fetch();
//
//    }
//
//    @Override
//    public List<Species> findSpeciesByEcNumber(String ecNumber) {
//        //JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .select(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId))
//                .from($)
//                .where($.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber)).distinct().orderBy($.scientificName.asc())
//                .fetch();
//
//    }
//
//    @Transactional(readOnly = true)
//    private List<String> findAccessionsByEC(String ecNumber) {
//        // JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .selectDistinct($.accession)
//                .from($)
//                .where($.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber))
//                .distinct()
//                .fetch();
//
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<Species> findSpeciesByEcNumberViaAccessions(String ecNumber, List<String> accessions) {
//        accessions = findAccessionsByEC(ecNumber);
//        //JPAQuery query = new JPAQuery(entityManager);
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .select(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId))
//                .from($)
//                .where($.accession.in(accessions)).distinct().orderBy($.scientificName.asc())
//                .distinct()
//                .fetch();
//
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Page<UniprotEntry> findPageableEntryByEc(String ec, Pageable pageable) {
//        //JPAQuery query = new JPAQuery(entityManager);
//        int pageSize = pageable.getPageSize();
//        int page = pageable.getPageNumber();
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        List<UniprotEntry> result = jpaQueryFactory
//                .selectFrom($)
//                .where($.enzymePortalEcNumbersSet.any().ecNumber.eq(ec))
//                .distinct().limit(pageSize).offset((page) * pageSize)
//                .fetch()
//                .stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());
//        return new PageImpl<>(result, pageable, result.size());
//
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<AssociatedProtein> findAssociatedProteinsByEC(String ecNumber, String entryType, int limit) {
//
//        Query query = entityManager.createNativeQuery("SELECT u.PROTEIN_NAME,u.ACCESSION,u.COMMON_NAME FROM UNIPROT_ENTRY u, ENZYME_PORTAL_EC_NUMBERS e WHERE e.UNIPROT_ACCESSION=u.ACCESSION AND e.EC_NUMBER='" + ecNumber + "' AND u.ENTRY_TYPE=" + entryType + " AND ROWNUM <=" + limit + "", "associatedProteins");
//
//        return query.getResultList();
//
//    }
//
//    @Override
//    public List<AssociatedProtein> findAssociatedProteinsByEC(String ecNumber, int limit) {
//
//        Query query = entityManager.createNativeQuery("SELECT u.ACCESSION,u.PROTEIN_NAME,u.COMMON_NAME FROM UNIPROT_ENTRY u, ENZYME_PORTAL_EC_NUMBERS e WHERE e.UNIPROT_ACCESSION=u.ACCESSION AND e.EC_NUMBER='" + ecNumber + "' ORDER BY u.ENTRY_TYPE", "associatedProteins");
//
//        @SuppressWarnings("unchecked")
//        List<AssociatedProtein> associatedProteins = query.getResultList();
//        return associatedProteins.stream().
//                limit(limit).collect(Collectors.toList());
//
//    }
//
//    @Override
//    public Page<UniprotEntry> findEnzymesWithAccessions(List<String> accessions, Pageable pageable) {
//    JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        long total = jpaQueryFactory
//                .selectFrom($).distinct()
//                .where($.accession.in(accessions)).fetchCount();
//        
//              
//        int pageSize = pageable.getPageSize();
//        int page = pageable.getPageNumber();
//    
//        List<UniprotEntry> result = jpaQueryFactory
//                .selectFrom($).distinct()
//                .where($.accession.in(accessions))
//                .limit(pageSize).offset((page) * pageSize)
//                .fetch().stream().collect(Collectors.toList());
//        //.stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());
//        return new PageImpl<>(result, pageable, total);
//    }
//
//    @Override
//    public Page<UniprotEntry> findEnzymesWithAccessions(List<String> accessions, Pageable pageable, UniprotEntryRepository uniprotEntryRepository) {
//
//         Predicate predicate =$.accession.in(accessions);
////        BooleanBuilder builders = new BooleanBuilder();
////        accessions.stream().forEach(acc -> {
////
////            builders.or($.accession.eq(acc));
////
////        });
//         
////           BooleanExpression exp = invoice.supplier.number.in(JPAExpressions.selectFrom(company‌​)
////                   .where(company.active.isTrue()) .select(company.nu‌​mber));
//    
//
//        return uniprotEntryRepository.findAll(predicate, pageable);
//    }
//    
//    
//    
//     private  List<UniprotEntry> filterFacets(List<String> accessions, List<Long> taxIds, List<String> compoundIds, List<String> omimNumbers, List<Integer> ecNumbers) {
//         
//          JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return  jpaQueryFactory.selectFrom($)
//                  .where($.enzymePortalEcNumbersSet.any().ecFamily.in(ecNumbers)
//                          .or(null)).fetch();
//     }
//
//     
//         @Transactional(readOnly = true)
//    @Override
//    public List<Species> findSpeciesInAccessions( List<String> accessions) {
//    
//        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//        return jpaQueryFactory
//                .select(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId))
//                .from($)
//                .where($.accession.in(accessions))
//                .distinct()
//                .fetch();
//
//    }

}
