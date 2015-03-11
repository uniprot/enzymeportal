/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import java.util.List;
import java.util.stream.Collectors;
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
        JPAQuery query = new JPAQuery(entityManager);

        List<String> entries = query.from($).where($.reactionId.equalsIgnoreCase(reactionId)).list($.uniprotAccession.accession)
                .stream().distinct().collect(Collectors.toList());

        return entries;

    }

//    @Override
//    public List<EnzymeReaction> findReactionsByAccession(String accession) {
//
//        JPAQuery query = new JPAQuery(entityManager);
//
////        List<EnzymeReaction> reactions = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession))
////                .list(Projections.constructor(EnzymeReaction.class, $.reactionId, $.reactionName));
////               
//        List<EnzymePortalReaction> enzreactions = query.from($).
//                where($.uniprotAccession.accession.equalsIgnoreCase(accession)).distinct()
//                .list($);
//        List<EnzymeReaction> reactions = new ArrayList<>();
//        if (enzreactions != null) {
//            for (EnzymePortalReaction r : enzreactions) {
//                EnzymeReaction er = new EnzymeReaction(r.getReactionId(), r.getReactionName());
//                reactions.add(er);
//            }
//
//            return reactions;
//        }
//
//        //return reactions;
//        return new ArrayList<>();
//    }

}
