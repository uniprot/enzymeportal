/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long>, QueryDslPredicateExecutor<UniprotEntry>, UniprotEntryRepositoryCustom {

//     @EntityGraph(value = "UniprotEntryEntityGraph", type = EntityGraphType.LOAD)
    UniprotEntry findByAccession(String accession);

    @Transactional(readOnly = true)
    @EntityGraph(value = "UniprotEntryEntityGraph", type = EntityGraph.EntityGraphType.LOAD)
    //@Query(value = "SELECT * FROM UNIPROT_ENTRY WHERE ACCESSION = :ACCESSION ", nativeQuery = true)
    @Query(value = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession")
    UniprotEntry findEnzymeByAccession(@Param("accession") String accession);

    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY WHERE ACCESSION IS NOT NULL", nativeQuery = true)
    List<String> findAccessions();

    @Query(value = "SELECT * FROM UNIPROT_ENTRY", nativeQuery = true)
    List<UniprotEntry> findUniprotEntries();

    @Query(value = "SELECT * from UNIPROT_ENTRY u JOIN ENZYME_PORTAL_EC_NUMBERS e ON u.ACCESSION=e.UNIPROT_ACCESSION WHERE e.EC_NUMBER= :EC_NUMBER", nativeQuery = true)
    List<UniprotEntry> findEnzymesByEc(@Param("EC_NUMBER") String ecNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CATALYTIC_ACTIVITY FROM ENZYME_CATALYTIC_ACTIVITY WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findCatalyticActivitiesByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Transactional(readOnly = true)
     @Query(value = "SELECT DISTINCT * FROM UNIPROT_ENTRY WHERE ACCESSION IN (:ACCESSION)", nativeQuery = true)
    List<UniprotEntry> findSummariesByAccessions(@Param("ACCESSION")List<String> accession);
    
        @Transactional(readOnly = true)
   // @EntityGraph(value = "UniprotEntryEntityGraph", type = EntityGraph.EntityGraphType.LOAD)
    Page<UniprotEntry> findByAccessionIn(List<String> accessions,Pageable pageable);

}
