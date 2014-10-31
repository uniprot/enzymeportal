/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
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
        List<String> enzymes = new ArrayList<>();
        JPAQuery query = new JPAQuery(entityManager);

        List<EnzymePortalReaction> entries = query.from($).where($.reactionId.equalsIgnoreCase(reactionId)).distinct().list($)
                .stream().distinct().collect(Collectors.toList());

        entries.stream().forEach((e) -> {
            enzymes.add(e.getUniprotAccession().getAccession());
        });

        return enzymes;

    }

}
