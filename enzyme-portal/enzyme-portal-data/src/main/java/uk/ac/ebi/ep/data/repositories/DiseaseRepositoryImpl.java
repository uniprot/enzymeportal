/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.StringExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.QEnzymePortalDisease;
import uk.ac.ebi.ep.data.search.model.Disease;

/**
 *
 * @author joseph
 */
public class DiseaseRepositoryImpl implements DiseaseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalDisease $ = QEnzymePortalDisease.enzymePortalDisease;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnzymePortalDisease> findDiseasesByNamePrefixes(List<String> name_prefixes) {
        JPAQuery query = new JPAQuery(entityManager);

        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        name_prefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.distinct().list($);

    }

    @Transactional(readOnly = true)
    @Override
    public List<EnzymePortalDisease> findDiseasesByAccessions(List<String> accessions) {
        JPAQuery query = new JPAQuery(entityManager);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.uniprotAccession.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);

        return query.distinct().list($);
    }

//    @Transactional(readOnly = true)
//    @Override
//    public List<EnzymePortalDisease> findDiseasesByAccession(String accession) {
//        JPAQuery query = new JPAQuery(entityManager);
//        List<EnzymePortalDisease> diseases = query.from($)
//                .where($.uniprotAccession.accession.equalsIgnoreCase(accession)).list($).stream().distinct().collect(Collectors.toList());
//        return diseases;
//    }

    /**
     * Note: meshId is used as default disease id for now as some omim entries
     * are null and we don't have efo yet
     *
     * @param meshId
     *
     * @return list of accessions
     */
    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByMeshId(String meshId) {

        List<String> enzymes = new ArrayList<>();
        JPAQuery query = new JPAQuery(entityManager);

        List<String> entries = query.from($).where($.meshId.equalsIgnoreCase(meshId)).list($.uniprotAccession.accession)
                .stream().distinct().collect(Collectors.toList());

//        entries.stream().forEach((e) -> {
//            enzymes.add(e.getUniprotAccession().getAccession());
//        });
        return entries;

    }

    @Override
    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseases() {
        EntityGraph eGraph = entityManager.getEntityGraph("DiseaseEntityGraph");
        eGraph.addAttributeNodes("uniprotAccession");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);
        List<EnzymePortalDisease> diseases = query.from($).list($);
        return diseases;
    }

   
    @Deprecated
    public List<String> findDiseasesByName(String name) {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from($)
                .where($.diseaseName.like(name)).distinct()
                .list($.diseaseName);
    }

    @Override
     @Transactional(readOnly = true)
    public List<Disease> findDiseasesByTaxId(Long taxId) {

        EntityGraph eGraph = entityManager.getEntityGraph("DiseaseEntityGraph");
        eGraph.addAttributeNodes("uniprotAccession");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);
        List<Disease> result = query.from($).where($.uniprotAccession.taxId.eq(taxId))
                .list(Projections.constructor(Disease.class, $.meshId, $.diseaseName, $.url)).stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Disease> findDiseasesNameLike(String name) {

        JPAQuery query = new JPAQuery(entityManager);
       
        List<Disease> result = query.from($).where($.diseaseName.like(name))
                .list(Projections.constructor(Disease.class, $.meshId, $.diseaseName)).stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Disease> findDiseasesByAccession(String accession) {
              JPAQuery query = new JPAQuery(entityManager);
     
        List<Disease> result = query.from($).where($.uniprotAccession.accession.equalsIgnoreCase(accession))
                .list(Projections.constructor(Disease.class, $.meshId, $.diseaseName,$.definition,$.url,$.evidence));

        return result.stream().distinct().collect(Collectors.toList()); 
    }

}
