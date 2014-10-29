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
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.QEnzymePortalDisease;

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
 @Transactional(readOnly = true)
    @Override
    public List<EnzymePortalDisease> findDiseasesByAccession(String accession) {
        JPAQuery query = new JPAQuery(entityManager);
        List<EnzymePortalDisease> diseases = query.from($)
                .where($.uniprotAccession.accession.equalsIgnoreCase(accession)).list($).stream().distinct().collect(Collectors.toList());
        return diseases;
    }

    /**
     * Note: meshId is used as default disease id for now as some omim entries
     * are null and we don't have efo yet
     *
     * @param diseaseId meshId is used as default at the moment
     * @return list of accessions
     */
     @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByDisease(String diseaseId) {

        JPAQuery query = new JPAQuery(entityManager);

        List<String> enzymes = query.from($).where($.meshId.equalsIgnoreCase(diseaseId)).list($.uniprotAccession.accession);
        return enzymes;

    }

    @Override
     @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseases() {
        EntityGraph eGraph = entityManager.getEntityGraph("DiseaseEntityGraph");
        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.loadgraph", eGraph);
        List<EnzymePortalDisease> diseases = query.from($).list($);
        return diseases;
    }

}
