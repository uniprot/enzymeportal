/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.repository;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface EnzymePortalUniqueEcRepository extends JpaRepository<EnzymePortalUniqueEc, Long> {

    @Query(value = "SELECT COUNT(DISTINCT EC_NUMBER) FROM ENZYME_PORTAL_UNIQUE_EC", nativeQuery = true)
    Long countEnzymes();

    @Query(value = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.ecNumber= :ecNumber")
    Stream<EnzymePortalUniqueEc> streamEnzymesByEc(@Param("ecNumber") String ecNumber);

    @Query(value = "SELECT e FROM EnzymePortalUniqueEc e")
    Stream<EnzymePortalUniqueEc> streamEnzymes();

    @Async
    @Query(value = "SELECT e FROM EnzymePortalUniqueEc e WHERE e.ecFamily = :ecFamily")
    CompletableFuture<EnzymePortalUniqueEc> findCompletableFutureEnzymesByEcClass(@Param("ecFamily") Short ecFamily);

}
