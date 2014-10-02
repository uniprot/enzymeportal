/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.QEnzymePortalPathways;

/**
 *
 * @author joseph
 */
public class EnzymePortalPathwaysRepositoryImpl implements EnzymePortalPathwaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalPathways $ = QEnzymePortalPathways.enzymePortalPathways;

    @Override
    public List<EnzymePortalPathways> findPathwaysByAccession(String accession) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanExpression isAccession = $.uniprotAccession.accession.equalsIgnoreCase(accession);
        List<EnzymePortalPathways> pathways = query.from($).where(isAccession).list($);

        return pathways.stream().distinct().collect(Collectors.toList());

    }

}
