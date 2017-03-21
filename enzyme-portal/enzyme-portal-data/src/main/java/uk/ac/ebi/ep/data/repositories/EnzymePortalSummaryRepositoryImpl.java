/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QEnzymePortalSummary;
import uk.ac.ebi.ep.data.entry.Summary;

/**
 *
 * @author joseph
 */
public class EnzymePortalSummaryRepositoryImpl implements EnzymePortalSummaryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalSummary $ = QEnzymePortalSummary.enzymePortalSummary;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transactional(readOnly = true)
    @Override
    public List<Summary> findSummariesByCommentType(String commentType) {

        String nativeQuery = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE";
        Query query = entityManager.createNativeQuery(nativeQuery, "findCommentTextAndAccession");
        List<Summary> result = query.setParameter("COMMENT_TYPE", commentType).getResultList();

        return result;
    }
    
        @Override
    public List<Summary> findSummariesByCommentType(String commentType, int limit) {

        String nativeQuery = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE";
        Query query = entityManager.createNativeQuery(nativeQuery, "findCommentTextAndAccession");
        List<Summary> result = query.setParameter("COMMENT_TYPE", commentType).setMaxResults(limit).getResultList();

        return result;
    }

//    public List<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions) {
//        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
//
//
//        JPAQuery query = new JPAQuery(entityManager);
//
//        query.setHint("javax.persistence.fetchgraph", eGraph);
//         //query.setHint("javax.persistence.loadgraph", eGraph);
//
//        BooleanBuilder builder = new BooleanBuilder();
//        accessions.stream().forEach(accession -> {
//
//            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));
//
//        });
//        query.from($).where(builder);
//        
//
//
//        return query.list($);
//    }
//
// 
//    public List<EnzymePortalSummary> findEnzymesByNamePrefixes(List<String> namePrefixes) {
//        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
//
//
//        
//        JPAQuery query = new JPAQuery(entityManager);
//        //query.setHint("javax.persistence.fetchgraph", eGraph);
//        query.setHint("javax.persistence.loadgraph", eGraph);
//
//       // StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
//        StringExpression idPrefix = $.uniprotAccession.relatedProteinsId.namePrefix;
//        
//        BooleanBuilder builder = new BooleanBuilder();
//        namePrefixes.stream().forEach((prefix) -> {
//
//            builder.or(idPrefix.equalsIgnoreCase(prefix));
//
//        });
//        query.from($).where(builder);
//        
//
//        
// 
//        
//        
//        return query.list($);
//        
//        
//
//    }
//
//
//
// 
//    public EnzymePortalSummary findEnzymeSummaryByAccession(String accession) {
//        //EntityGraph eGraph = entityManager.createEntityGraph(EnzymePortalSummary.class);
//        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
//
//        eGraph.addAttributeNodes("uniprotAccession");
//
//       eGraph.addSubgraph("uniprotAccession")
//                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
//                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
//        JPAQuery query = new JPAQuery(entityManager);
//
//        query.setHint("javax.persistence.fetchgraph", eGraph);
//        EnzymePortalSummary e = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession)).singleResult($);
//
//        return e;
//    }
//
//
//    public Page<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions, Pageable pageable) {
//        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
//        eGraph.addAttributeNodes("uniprotAccession");
//
//        eGraph.addSubgraph("uniprotAccession")
//                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
//                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
//        JPAQuery query = new JPAQuery(entityManager);
//        query.setHint("javax.persistence.fetchgraph", eGraph);
//
//        BooleanBuilder builder = new BooleanBuilder();
//        accessions.stream().forEach((accession) -> {
//
//            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));
//
//        });
//        query.from($).where(builder);
//        List<EnzymePortalSummary> result = query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());
//
//        return new PageImpl(result, pageable, result.size());
//
//    }
//
//
//    public List<EnzymePortalSummary> findEnzymeSummariesByAccession(String accession) {
//        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
//
//        eGraph.addAttributeNodes("uniprotAccession");
//       eGraph.addSubgraph("uniprotAccession")
//                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
//                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
//        JPAQuery query = new JPAQuery(entityManager);
//
//        query.setHint("javax.persistence.fetchgraph", eGraph);
//        List<EnzymePortalSummary> summaries = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession)).list($);
//        return summaries;
//
//    }
//
//    
}
