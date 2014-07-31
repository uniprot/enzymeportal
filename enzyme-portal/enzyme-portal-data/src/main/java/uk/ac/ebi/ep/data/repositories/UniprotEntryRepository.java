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
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long>,  QueryDslPredicateExecutor<UniprotEntry>, UniprotEntryRepositoryCustom {
   
    UniprotEntry findByAccession(String accession);
     @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY WHERE ACCESSION IS NOT NULL", nativeQuery = true)
            //AND ROWNUM <= 2000
    List<String> findAccession();
}
