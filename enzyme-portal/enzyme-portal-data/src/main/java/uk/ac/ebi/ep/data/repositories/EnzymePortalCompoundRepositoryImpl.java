/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.QEnzymePortalCompound;

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

}
