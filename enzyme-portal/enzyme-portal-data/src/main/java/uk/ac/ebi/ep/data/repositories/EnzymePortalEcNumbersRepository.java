/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;


/**
 *
 * @author joseph
 */
public interface EnzymePortalEcNumbersRepository extends JpaRepository<EnzymePortalEcNumbers, Long>, QueryDslPredicateExecutor<EnzymePortalEcNumbers>, EnzymePortalEcNumbersRepositoryCustom {
    
}
