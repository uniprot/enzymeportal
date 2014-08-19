/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.QEnzymePortalSummary;

/**
 *
 * @author joseph
 */
public class EnzymeSummaryRepositoryImpl implements EnzymeSummaryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalSummary $ = QEnzymePortalSummary.enzymePortalSummary;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EnzymePortalSummary> findByCommentType(String commentType, int limit) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression condition = ($.commentType.equalsIgnoreCase(commentType)
                .and($.commentText.isNotEmpty()).and($.uniprotAccession.isNotNull()));
        List<EnzymePortalSummary> summariesByCommentType = query.from($).where(condition).limit(limit).distinct().list($);
        return summariesByCommentType;

    }

    @Override
    public List<EnzymePortalSummary> findByCommentType(String commentType) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression condition = ($.commentType.equalsIgnoreCase(commentType)
                .and($.commentText.isNotEmpty()).and($.uniprotAccession.isNotNull()));

        List<EnzymePortalSummary> summariesByCommentType = query.from($).where(condition).distinct().list($);
        return summariesByCommentType;

    }

    @Override
    public List<EnzymePortalSummary> findByCommentText(String commentText, int limit) {
        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression condition = ($.commentText.equalsIgnoreCase(commentText).and($.commentText.isNotEmpty()));
        List<EnzymePortalSummary> summary = query.from($).where(condition).limit(limit).distinct().list($);
        return summary;

    }

    @Override
    public EnzymePortalSummary findDiseaseEvidence(String accession) {
        JPAQuery query = new JPAQuery(entityManager);
        String commentType = "DISEASE";
        EnzymePortalSummary summary = query.from($).
                where($.commentType.equalsIgnoreCase(commentType).
                        and($.uniprotAccession.accession.equalsIgnoreCase(accession))).singleResult($);

        return summary;
    }

//    @Override
//    public EnzymePortalSummary findByAccession(String accession) {
//        String commentType = "EC_NUMBER";
//        JPAQuery query = new JPAQuery(entityManager);
//        BooleanExpression isEnzyme = $.commentType.equalsIgnoreCase(commentType);
//        EnzymePortalSummary summary = query.from($).
//                where(isEnzyme.and($.uniprotAccession.accession.equalsIgnoreCase(accession))).singleResult($);
//        return summary;
//    }
 

    @Override
    public List<EnzymePortalSummary> findEnzymesByAccessions(List<String> accessions) {
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
        eGraph.addAttributeNodes("uniprotAccession");
        eGraph.addSubgraph("uniprotAccession")
                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        return query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());

    }
    
       @Override
    public List<EnzymePortalSummary> findEnzymesByNamePrefixes(List<String> name_prefixes) {
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
        eGraph.addAttributeNodes("uniprotAccession");
        //eGraph.addSubclassSubgraph( UniprotEntry.class);
        //eGraph.addSubgraph("uniprotAccession", UniprotEntry.class).addAttributeNodes("uk.ac.ebi.ep.data.domain.UniprotEntry.enzymePortalDiseaseSet");
        eGraph.addSubgraph("uniprotAccession")
                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");
           System.out.println("alternatives "+ eGraph.getName());
        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.loadgraph", eGraph);

        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        name_prefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());
    }

    @Override
    public EnzymePortalSummary findByAccession(String accession) {
        //EntityGraph eGraph = entityManager.createEntityGraph(EnzymePortalSummary.class);
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");

        eGraph.addAttributeNodes("uniprotAccession");
        eGraph.addSubgraph("uniprotAccession")
                 .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");



        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        EnzymePortalSummary e = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession)).singleResult($);
        return e;
    }

}
