/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.StringExpression;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public class UniprotEntryRepositoryImpl implements UniprotEntryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QUniprotEntry $ = QUniprotEntry.uniprotEntry;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

//    @Override
//    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> name_prefixes) {
////        //String commentType="EC_NUMBER";
////        JPAQuery query = new JPAQuery(entityManager);
////        //QEnzymePortalSummary s = QEnzymePortalSummary.enzymePortalSummary;
//////.innerJoin(mate).on(cat.mateId.eq(mate.id))
////        StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
////        BooleanBuilder builder = new BooleanBuilder();
////        name_prefixes.stream().forEach((prefix) -> {
////            //builder.or(idPrefix.e .equalsIgnoreCase(prefix));
////            builder.or(idPrefix.equalsIgnoreCase(prefix));
////              //builder.or($.name.equalsIgnoreCase(prefix));
////        });
////        query.from($).where(builder);//.innerJoin($.enzymePortalSummarySet,s).where(builder);
////        //and(s.commentType.equalsIgnoreCase(commentType))
////        return query.list($);
//        
//        return findByNamePrefixes(name_prefixes);
//
//    }
//    @Override
//    public List<Species> getSpecies(List<String> name_prefixes) {
//     
//         JPAQuery query = new JPAQuery(entityManager);
//          BooleanExpression condition = null;
//          
//           List<UniprotEntry> enzymes = findByNamePrefixes(name_prefixes);
//           
//          
//    
//    }
    @Override
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> name_prefixes) {
        JPAQuery query = new JPAQuery(entityManager);

        StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        name_prefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.list($);
    }

    @Override
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {
        JPAQuery query = new JPAQuery(entityManager);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        return query.distinct().list($);
    }

}
