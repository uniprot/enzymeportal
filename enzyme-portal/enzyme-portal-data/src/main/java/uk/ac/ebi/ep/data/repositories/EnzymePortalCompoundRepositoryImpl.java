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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
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
        BooleanExpression isUniprotName = $.uniprotAccession.name.equalsIgnoreCase(uniprotName);

        List<EnzymePortalCompound> compounds = query.from($).where(isUniprotName).distinct().list($);

        return compounds;

    }

    @Override
    public List<EnzymePortalCompound> findCompoundsByNameprefixes(List<String> namePrefixes) {

        JPAQuery query = new JPAQuery(entityManager);

        StringExpression idPrefix = $.uniprotAccession.name.substring(0, $.uniprotAccession.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.distinct().list($);

    }

    @Override
    public List<EnzymePortalCompound> findCompoundsByUniprotAccession(String accession) {
        JPAQuery query = new JPAQuery(entityManager);
        BooleanExpression isUniprotAcc = $.uniprotAccession.accession.equalsIgnoreCase(accession);

        List<EnzymePortalCompound> compounds = query.from($).where(isUniprotAcc).distinct().list($);

        return compounds;
    }

    @Override
    public List<String> findEnzymesByCompound(String compoundId) {

        Set<String> enzymes = new TreeSet<>();
        JPAQuery query = new JPAQuery(entityManager);
        BooleanExpression compound = $.compoundId.equalsIgnoreCase(compoundId);

        List<EnzymePortalCompound> compounds = query.from($).where(compound).distinct().list($);

        compounds.parallelStream().forEach((c) -> {
            enzymes.add(c.getUniprotAccession().getAccession());
        });

        return enzymes.stream().distinct().collect(Collectors.toList());
    }
    
    
    
        @Override
     @Transactional(readOnly = true)
    public List<Compound> findCompoundsByTaxId(Long taxId) {

        EntityGraph eGraph = entityManager.getEntityGraph("CompoundEntityGraph");
        eGraph.addAttributeNodes("uniprotAccession");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);
        List<Compound> result = query.from($).where($.uniprotAccession.taxId.eq(taxId)).distinct()
                .list(Projections.constructor(Compound.class, $.compoundId, $.compoundName, $.url,$.compoundRole)).stream().distinct().collect(Collectors.toList());

        return result;
    }

}
