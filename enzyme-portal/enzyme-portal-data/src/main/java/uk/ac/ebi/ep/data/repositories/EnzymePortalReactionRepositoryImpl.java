/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.data.domain.QEnzymePortalReaction;

/**
 *
 * @author joseph
 */
public class EnzymePortalReactionRepositoryImpl implements EnzymePortalReactionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalReaction $ = QEnzymePortalReaction.enzymePortalReaction;

    @Override
    public List<String> findAccessionsByReactionId(String reactionId) {
        //JPAQuery query = new JPAQuery(entityManager);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.selectDistinct($.uniprotAccession.accession)
                 .from($)
                .where($.reactionId.equalsIgnoreCase(reactionId))
                .fetch();

    }

}
