/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPADeleteClause;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QEnzymePortalDisease;

/**
 *
 * @author joseph
 */
public class DiseaseRepositoryImpl implements DiseaseRepositoryCustom{
    
          @PersistenceContext
    private EntityManager entityManager;
    private static final QEnzymePortalDisease $ = QEnzymePortalDisease.enzymePortalDisease;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
      @Transactional
    public void dropDiseaseDatabase() {
    // JPADeleteClause jpa = new JPADeleteClause(entityManager,$).where($.diseaseId.eq(BigDecimal.valueOf(8)));
      JPADeleteClause jpa = new JPADeleteClause(entityManager,$);
     jpa.execute();
     
    }
}
