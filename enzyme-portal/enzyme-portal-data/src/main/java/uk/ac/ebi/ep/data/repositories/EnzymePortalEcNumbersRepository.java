/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;

/**
 *
 * @author joseph
 */
public interface EnzymePortalEcNumbersRepository extends JpaRepository<EnzymePortalEcNumbers, Long>, QueryDslPredicateExecutor<EnzymePortalEcNumbers>, EnzymePortalEcNumbersRepositoryCustom {

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER = :EC_NUMBER", nativeQuery = true)
    List<EnzymePortalEcNumbers> findByEcNumber(@Param("EC_NUMBER") String ecNumber);
    
        @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalEcNumbers> findByEcNumbersByAccession(@Param("UNIPROT_ACCESSION") String ecNumber);
}
