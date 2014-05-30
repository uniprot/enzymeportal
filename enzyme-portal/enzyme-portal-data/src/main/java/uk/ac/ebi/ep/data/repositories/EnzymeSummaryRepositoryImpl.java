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
import uk.ac.ebi.ep.data.domain.EnzymeSummary;
import uk.ac.ebi.ep.data.domain.QEnzymeSummary;

/**
 *
 * @author joseph
 */
public class EnzymeSummaryRepositoryImpl implements EnzymeSummaryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymeSummary $ = QEnzymeSummary.enzymeSummary;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EnzymeSummary> findByCommentType(String commentType, int limit) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression condition = ($.commentType.equalsIgnoreCase(commentType)
                .and($.commentText.isNotEmpty()).and($.accession.isNotNull()));
        List<EnzymeSummary> summariesByCommentType = query.from($).where(condition).limit(limit).distinct().list($);
        return summariesByCommentType;

    }

    @Override
    public List<EnzymeSummary> findByCommentType(String commentType) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression condition = ($.commentType.equalsIgnoreCase(commentType)
                .and($.commentText.isNotEmpty()).and($.accession.isNotNull()));

        List<EnzymeSummary> summariesByCommentType = query.from($).where(condition).distinct().list($);
        return summariesByCommentType;

    }

    @Override
    public List<EnzymeSummary> findByCommentText(String commentText, int limit) {
        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression condition = ($.commentText.equalsIgnoreCase(commentText).and($.commentText.isNotEmpty()));
        List<EnzymeSummary> summary = query.from($).where(condition).limit(limit).distinct().list($);
        return summary;

    }

}
