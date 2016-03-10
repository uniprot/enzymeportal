/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.QEnzymePortalCompound;
import uk.ac.ebi.ep.data.search.model.Compound;

/**
 *
 * @author joseph
 */
public class EnzymePortalCompoundRepositoryImpl implements EnzymePortalCompoundRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalCompound $ = QEnzymePortalCompound.enzymePortalCompound;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public List<EnzymePortalCompound> findCompoundsByUniprotName(String uniprotName) {

        JPAQuery query = new JPAQuery(entityManager);
        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanExpression isUniprotName = idPrefix.equalsIgnoreCase(uniprotName);

        return query.from($).where(isUniprotName).distinct().list($);

    }

    @Override
    public List<EnzymePortalCompound> findCompoundsByNameprefixes(List<String> namePrefixes) {

        JPAQuery query = new JPAQuery(entityManager);

        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach(prefix -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.distinct().list($);

    }

    @Override
    public List<EnzymePortalCompound> findCompoundsByUniprotAccession(String accession) {
        JPAQuery query = new JPAQuery(entityManager);
        BooleanExpression isUniprotAcc = $.uniprotAccession.accession.equalsIgnoreCase(accession);

        return query.from($).where(isUniprotAcc).distinct().list($);

    }


    @Override
    @Transactional(readOnly = true)
    public List<Compound> findCompoundsByTaxId(Long taxId) {

        //EntityGraph eGraph = entityManager.getEntityGraph("CompoundEntityGraph");
        //eGraph.addAttributeNodes("uniprotAccession");

        JPAQuery query = new JPAQuery(entityManager);
       // query.setHint("javax.persistence.fetchgraph", eGraph);
        List<Compound> result = query.from($).where($.uniprotAccession.taxId.eq(taxId)).distinct()
                .list(Projections.constructor(Compound.class, $.compoundId, $.compoundName, $.url, $.compoundRole)).stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Compound> findCompoundsByEcNumber(String ecNumber) {
        String nativeQuery = "SELECT cp.COMPOUND_ID,cp.COMPOUND_NAME,cp.URL,cp.COMPOUND_ROLE FROM ENZYME_PORTAL_COMPOUND cp, ENZYME_PORTAL_EC_NUMBERS e \n"
                + "WHERE cp.UNIPROT_ACCESSION=e.UNIPROT_ACCESSION AND e.EC_NUMBER = :EC_NUMBER GROUP BY cp.COMPOUND_ID,cp.COMPOUND_NAME,cp.URL,cp.COMPOUND_ROLE";
        //Query query = entityManager.createNativeQuery(nativeQuery, "compoundMapping");
       // List<Compound> result = query.setParameter("EC_NUMBER", ecNumber).getResultList();
        
        
               JPAQuery query = new JPAQuery(entityManager);
   
        List<Compound> result = query.from($).where($.uniprotAccession.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber)).distinct()
                .list(Projections.constructor(Compound.class, $.compoundId, $.compoundName, $.url, $.compoundRole)).stream().distinct().collect(Collectors.toList());

        
        

        return result.stream().distinct().collect(Collectors.toList());

    }

}
