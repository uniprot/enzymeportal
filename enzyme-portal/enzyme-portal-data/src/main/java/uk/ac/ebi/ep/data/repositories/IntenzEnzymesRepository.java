/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface IntenzEnzymesRepository extends JpaRepository<IntenzEnzymes, Long>, QueryDslPredicateExecutor<IntenzEnzymes> {

    //@Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ *  FROM INTENZ_ENZYMES", nativeQuery = true)
    List<IntenzEnzymes> findIntenzEnzymes();

    @Query(value = "SELECT *  FROM INTENZ_ENZYMES WHERE TRANSFER_FLAG = 'N'", nativeQuery = true)
    List<IntenzEnzymes> findNonTransferredEnzymes();

    @Query("select e from IntenzEnzymes e")
    Stream<IntenzEnzymes> streamAllIntenzEnzymes();
}
