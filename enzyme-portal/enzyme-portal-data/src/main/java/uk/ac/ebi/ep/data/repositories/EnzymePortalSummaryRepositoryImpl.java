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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.QEnzymePortalSummary;

/**
 *
 * @author joseph
 */
public class EnzymePortalSummaryRepositoryImpl implements EnzymePortalSummaryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalSummary $ = QEnzymePortalSummary.enzymePortalSummary;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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
    public List<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions) {
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
        //eGraph.addAttributeNodes("uniprotAccession");

//        eGraph.addSubgraph("uniprotAccession")
//                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
//                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
         //query.setHint("javax.persistence.loadgraph", eGraph);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        


        return query.list($);
//        List<EnzymePortalSummary> result = query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());
//        Pageable pageable = new PageRequest(0, 10);
//        System.out.println("enzyme summary repositoryImpl Line 166 : FIXME");
//        Page page = new PageImpl(result, pageable, result.size());
//        return page.getContent();
    }

    @Override
    public List<EnzymePortalSummary> findEnzymesByNamePrefixes(List<String> name_prefixes) {
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");

        //EntityGraph eGraph = entityManager.createEntityGraph("summary.graph");
        
        //eGraph.addSubgraph("uniprotAccession").addAttributeNodes("enzymePortalCompoundSet");
         //eGraph.addSubgraph("uniprotAccession").addAttributeNodes("relatedProteinsId");
        

 

//        eGraph.addSubgraph("uniprotAccession")
//                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
//                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
//                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
       
       
        
        JPAQuery query = new JPAQuery(entityManager);
        //query.setHint("javax.persistence.fetchgraph", eGraph);
        query.setHint("javax.persistence.loadgraph", eGraph);

       // StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        StringExpression idPrefix = $.uniprotAccession.relatedProteinsId.namePrefix;
        
        BooleanBuilder builder = new BooleanBuilder();
        name_prefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        

        
 
        
        
        return query.list($);
        
        
        
        
        
//        List<EnzymePortalSummary> result = query.distinct().list($).stream().distinct().collect(Collectors.toList());
//        Pageable pageable = new PageRequest(0, 50);
//        System.out.println("enzyme summary repositoryImpl Line 166 : FIXME" + result.size());
//        Page page = new PageImpl(result, pageable, result.size());
//   
//        return page.getContent();
    }



    @Override
    public EnzymePortalSummary findEnzymeSummaryByAccession(String accession) {
        //EntityGraph eGraph = entityManager.createEntityGraph(EnzymePortalSummary.class);
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");

        eGraph.addAttributeNodes("uniprotAccession");

       eGraph.addSubgraph("uniprotAccession")
                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        EnzymePortalSummary e = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession)).singleResult($);

        return e;
    }

    @Override
    public Page<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions, Pageable pageable) {
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");
        eGraph.addAttributeNodes("uniprotAccession");

        eGraph.addSubgraph("uniprotAccession")
                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        List<EnzymePortalSummary> result = query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());

        return new PageImpl(result, pageable, result.size());

    }

    @Override
    public List<EnzymePortalSummary> findEnzymeSummariesByAccession(String accession) {
        EntityGraph eGraph = entityManager.getEntityGraph("summary.graph");

        eGraph.addAttributeNodes("uniprotAccession");
       eGraph.addSubgraph("uniprotAccession")
                .addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                        "enzymePortalSummarySet", "enzymePortalDiseaseSet",
                        "enzymePortalCompoundSet", "uniprotXrefSet", "enzymePortalEcNumbersSet");
        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        List<EnzymePortalSummary> summaries = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession)).list($);
        return summaries;

    }

    
    
}
