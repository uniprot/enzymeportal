/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import uk.ac.ebi.ep.xml.entity.EnzymePortalEcNumbers;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface EnzymePortalEcNumbersRepository extends JpaRepository<EnzymePortalEcNumbers, Long> {

    @Query(value = "SELECT COUNT(DISTINCT EC_NUMBER) FROM ENZYME_PORTAL_EC_NUMBERS", nativeQuery = true)
    Long countEnzymes();

    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_NUMBER = :EC_NUMBER", nativeQuery = true)
    List<EnzymePortalEcNumbers> findByEcNumber(@Param("EC_NUMBER") String ecNumber);

    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE ROWNUM <= :LIMIT", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymePortalEcNumbers(@Param("LIMIT") Integer limit);

    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymePortalEcNumbers();

    @Async
    @Query(value = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecFamily = :ecFamily")
    CompletableFuture<EnzymePortalEcNumbers> findCompletableFutureEnzymesByEcClass(@Param("ecFamily") Short ecFamily);

    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_FAMILY = :EC_FAMILY", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymesByEcClass(@Param("EC_FAMILY") Integer ecFamily);

    @Query(value = "SELECT * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_FAMILY = :EC_FAMILY AND ROWNUM <= :LIMIT", nativeQuery = true)
    List<EnzymePortalEcNumbers> findEnzymesByEcClass(@Param("EC_FAMILY") Integer ecFamily, @Param("LIMIT") Integer limit);

}
