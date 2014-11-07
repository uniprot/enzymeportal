/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
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
    
    
       @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByPathwayId(String pathwayId) {

        List<String> enzymes = new ArrayList<>();
        JPAQuery query = new JPAQuery(entityManager);

        List<EnzymePortalPathways> entries = query.from($).where($.pathwayId.equalsIgnoreCase(pathwayId)).distinct().list($)
                .stream().distinct().collect(Collectors.toList());
        
       
        entries.stream().forEach((e) -> {
            enzymes.add(e.getUniprotAccession().getAccession());
        });

        return enzymes;

    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findPathwaysByName(String pathwayName) {
        JPAQuery query = new JPAQuery(entityManager);

        List<String> entries = query.from($).where($.pathwayName.like(pathwayName)).distinct().list($.pathwayName)
               .stream().distinct().collect(Collectors.toList());

        return entries;

    }
}
