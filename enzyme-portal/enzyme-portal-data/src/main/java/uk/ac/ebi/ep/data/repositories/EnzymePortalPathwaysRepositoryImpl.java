/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QEnzymePortalPathways;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;

/**
 *
 * @author joseph
 */
public class EnzymePortalPathwaysRepositoryImpl implements EnzymePortalPathwaysRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalPathways $ = QEnzymePortalPathways.enzymePortalPathways;

     @Transactional(readOnly = true)
   // @Override
    public List<Pathway> findPathwaysByAccession(String accession) {

        JPAQuery query = new JPAQuery(entityManager);
        
   
  List<Pathway> pathways = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession))
                .list(Projections.constructor(Pathway.class, $.pathwayId, $.pathwayName));
  
        return pathways;
    }

//    @Transactional(readOnly = true)
//    @Override
//    public List<String> findAccessionsByPathwayId(String pathwayId) {
//
//        List<String> enzymes = new ArrayList<>();
//        JPAQuery query = new JPAQuery(entityManager);
//
//        List<EnzymePortalPathways> entries = query.from($).where($.pathwayId.equalsIgnoreCase(pathwayId)).distinct().list($)
//                .stream().distinct().collect(Collectors.toList());
//
//        entries.stream().forEach(e -> {
//            enzymes.add(e.getUniprotAccession().getAccession());
//        });
//
//        return enzymes;
//
//    }

    @Transactional(readOnly = true)
    @Override
    public List<Pathway> findPathwaysByName(String name) {
        JPAQuery query = new JPAQuery(entityManager);

          String pathwayName = String.format("%%%s%%", name).toLowerCase();
        List<Pathway> entries = query.from($).where($.pathwayName.toLowerCase().like(pathwayName))
                .list(Projections.constructor(Pathway.class,$.pathwayGroupId, $.pathwayId, $.pathwayName));

        return entries.stream().distinct().collect(Collectors.toList());

    }
}
