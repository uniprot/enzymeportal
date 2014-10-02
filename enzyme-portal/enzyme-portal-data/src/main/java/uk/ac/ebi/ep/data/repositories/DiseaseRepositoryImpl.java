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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

//    @Override
//    @Transactional
//    public void dropDiseaseDatabase() {
//        // JPADeleteClause jpa = new JPADeleteClause(entityManager,$).where($.diseaseId.eq(BigDecimal.valueOf(8)));
//        JPADeleteClause jpa = new JPADeleteClause(entityManager, $);
//        jpa.execute();
//
//    }

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

    @Override
    public List<EnzymePortalDisease> findDiseasesByAccession(String accession) {
        JPAQuery query = new JPAQuery(entityManager);
        List<EnzymePortalDisease> diseases = query.from($)
                .where($.uniprotAccession.accession.equalsIgnoreCase(accession)).list($).stream().distinct().collect(Collectors.toList());
        return diseases;
    }
    
}
