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
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
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
                EntityGraph eGraph =entityManager.getEntityGraph("graph.UniprotEntry");
        
           eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");   
        
         
        
       
        
        JPAQuery query = new JPAQuery(entityManager);
        
        query.setHint("javax.persistence.fetchgraph", eGraph);

        StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        name_prefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.list($).parallelStream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {
        
                EntityGraph eGraph =entityManager.getEntityGraph("graph.UniprotEntry");
               
            eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");   
        
           
       
        
        JPAQuery query = new JPAQuery(entityManager);


        query.setHint("javax.persistence.fetchgraph", eGraph);
       
        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        return query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());
    }

    @Override
    public UniprotEntry findByAccession(String accession) {
   
                   EntityGraph eGraph =entityManager.getEntityGraph("graph.UniprotEntry");
               eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");   
        
               
          
        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        UniprotEntry e = query.from($).where($.accession.equalsIgnoreCase(accession)).singleResult($);
        return e;
    }

    @Override
    public List<UniprotEntry> findEnzymeByNamePrefixAndProteinName(String namePrefix,String proteinName) {
     
       EntityGraph eGraph = entityManager.getEntityGraph("graph.UniprotEntry");

       
  
        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalDiseaseSet", "enzymePortalCompoundSet", "uniprotXrefSet");   
        
        
        
          JPAQuery query = new JPAQuery(entityManager);
          query.setHint("javax.persistence.fetchgraph", eGraph);
        

        StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        
       

            builder.or(idPrefix.equalsIgnoreCase(namePrefix));

        
        query.from($).where(builder.and($.proteinName.equalsIgnoreCase(proteinName)));
        return query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());
    
    }



}
