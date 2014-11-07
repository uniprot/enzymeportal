/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;

/**
 *
 * @author joseph
 */
public interface DiseaseRepository extends JpaRepository<EnzymePortalDisease, Long>, QueryDslPredicateExecutor<EnzymePortalDisease>, DiseaseRepositoryCustom{
    
    EnzymePortalDisease findByDiseaseId(BigDecimal id);
    List<EnzymePortalDisease> findByDiseaseNameLikeIgnoreCase(String diseaseName);
}
