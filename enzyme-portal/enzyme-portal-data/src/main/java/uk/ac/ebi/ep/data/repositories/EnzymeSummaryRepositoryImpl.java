/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import java.util.List;
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

    @Override
    public EnzymePortalSummary findByAccession(String accession) {
        String commentType="EC_NUMBER";
         JPAQuery query = new JPAQuery(entityManager);
          BooleanExpression isEnzyme = $.commentType.equalsIgnoreCase(commentType);
          EnzymePortalSummary summary = query.from($).
                  where(isEnzyme.and($.uniprotAccession.accession.equalsIgnoreCase(accession))).singleResult($);
          return summary;
    }

}
