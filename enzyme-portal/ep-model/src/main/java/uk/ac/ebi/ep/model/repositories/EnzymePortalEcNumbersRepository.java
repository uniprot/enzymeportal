/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalEcNumbers;

/**
 *
 * @author joseph
 */
public interface EnzymePortalEcNumbersRepository extends JpaRepository<EnzymePortalEcNumbers, Long>, QueryDslPredicateExecutor<EnzymePortalEcNumbers>, EnzymePortalEcNumbersRepositoryCustom {

    @Query(value = "SELECT /*+ PARALLEL(auto) */ COUNT(DISTINCT EC_NUMBER) FROM ENZYME_PORTAL_EC_NUMBERS", nativeQuery = true)
    Long countEnzymes();

    @Transactional(readOnly = true)
    //@Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER ='1.1.1.1'  AND ROWNUM <= :LIMIT", nativeQuery = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_EC_NUMBERS WHERE ROWNUM <= :LIMIT", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymePortalEcNumbers(@Param("LIMIT") Integer limit);

    @Transactional(readOnly = true)
    // @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER ='1.1.1.1' AND ROWNUM <=5 UNION SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER ='2.1.1.1' AND ROWNUM <=5", nativeQuery = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_EC_NUMBERS", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymePortalEcNumbers();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER = :EC_NUMBER", nativeQuery = true)
    List<EnzymePortalEcNumbers> findByEcNumber(@Param("EC_NUMBER") String ecNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_EC_NUMBERS WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalEcNumbers> findByEcNumbersByAccession(@Param("UNIPROT_ACCESSION") String ecNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */  DISTINCT(UNIPROT_ACCESSION) FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER = :EC_NUMBER", nativeQuery = true)
    List<String> findAccessionsByEcNumber(@Param("EC_NUMBER") String ecNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_FAMILY = :EC_FAMILY", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymesByEcClass(@Param("EC_FAMILY") Integer ecFamily);
    
        @Query(value = "SELECT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_FAMILY = :EC_FAMILY AND ROWNUM <= :LIMIT", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymesByEcClass(@Param("EC_FAMILY") Integer ecFamily, @Param("LIMIT") Integer limit);

}
