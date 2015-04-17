/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QEnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.search.model.EcNumber;

/**
 *
 * @author joseph
 */
public class EnzymePortalEcNumbersRepositoryImpl implements EnzymePortalEcNumbersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalEcNumbers $ = QEnzymePortalEcNumbers.enzymePortalEcNumbers;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByEc(String ecNumber) {
        JPAQuery query = new JPAQuery(entityManager);

        List<String> enzymes = query.from($).where($.ecNumber.equalsIgnoreCase(ecNumber))
                .list($.uniprotAccession.accession);

        return enzymes.stream().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EcNumber> findEnzymeFamiliesByTaxId(Long taxId) {

        EntityGraph eGraph = entityManager.getEntityGraph("EcNumberEntityGraph");
        eGraph.addAttributeNodes("uniprotAccession");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);

        List<EcNumber> result = query.from($).where($.uniprotAccession.taxId.eq(taxId))
                .list(Projections.constructor(EcNumber.class, $.ecFamily)).stream().distinct().collect(Collectors.toList());

        result.sort(SORT_BY_EC);
        return result;
    }
    public static Comparator<EcNumber> SORT_BY_EC = (EcNumber ec1, EcNumber ec2) -> ec1.getEc().compareTo(ec2.getEc());
}
