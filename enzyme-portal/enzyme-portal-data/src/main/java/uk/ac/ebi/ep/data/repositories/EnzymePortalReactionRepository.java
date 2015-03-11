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
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;


/**
 *
 * @author joseph
 */
public interface EnzymePortalReactionRepository extends JpaRepository<EnzymePortalReaction, Long>,   QueryDslPredicateExecutor<EnzymePortalReaction>, EnzymePortalReactionRepositoryCustom {
       @Query(value = "SELECT * FROM ENZYME_PORTAL_REACTION  WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalReaction> findReactionsByAccession(@Param("UNIPROT_ACCESSION") String accession);
}
