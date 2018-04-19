/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalReaction;

/**
 *
 * @author joseph
 */
public interface EnzymePortalReactionRepository extends JpaRepository<EnzymePortalReaction, Long>, QueryDslPredicateExecutor<EnzymePortalReaction> {//, EnzymePortalReactionRepositoryCustom {

    @Query(value = "SELECT * FROM ENZYME_PORTAL_REACTION  WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalReaction> findReactionsByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    @Query(value = "INSERT INTO ENZYME_PORTAL_REACTION  VALUES(SEQ_REACTION_INTERNAL_ID.nextval,?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
    void addRheaReaction(String rheaId, String reactionName, String reactionSource, String relationship, String accession, String url, String keggId);

}
