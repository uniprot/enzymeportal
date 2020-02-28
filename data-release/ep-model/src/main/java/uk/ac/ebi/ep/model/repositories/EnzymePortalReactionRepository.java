package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalReaction;

/**
 *
 * @author joseph
 */
public interface EnzymePortalReactionRepository extends JpaRepository<EnzymePortalReaction, Long> {

    @Query(value = "SELECT * FROM ENZYME_PORTAL_REACTION  WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalReaction> findReactionsByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    @Query(value = "INSERT INTO ENZYME_PORTAL_REACTION  VALUES(SEQ_REACTION_INTERNAL_ID.nextval,?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
    void addRheaReaction(String rheaId, String reactionName, String reactionSource, String relationship, String accession, String url, String keggId);

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)

    @Query(value = "UPDATE ENZYME_PORTAL_REACTION SET kegg_id = :kegg_id where reaction_id= :reaction_id", nativeQuery = true)
    void updateRheaReaction(@Param("kegg_id") String keggId, @Param("reaction_id") String reactionId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "alter table ENZYME_PORTAL_REACTION disable constraint REACTION_ACCESSION_FK", nativeQuery = true)
    void disableAccessionContraints();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "delete from ENZYME_PORTAL_REACTION where UNIPROT_ACCESSION not in (select accession from uniprot_entry)", nativeQuery = true)
    void deleteNonEnzymesReactions();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "alter table ENZYME_PORTAL_REACTION enable constraint REACTION_ACCESSION_FK", nativeQuery = true)
    void enableAccessionContraints();

    @Query(value = "SELECT COUNT(DISTINCT reaction_id) FROM ENZYME_PORTAL_REACTION", nativeQuery = true)
    long countUniqueRheaIds();

    @Query(value = "SELECT COUNT(distinct uniprot_accession) FROM ENZYME_PORTAL_REACTION", nativeQuery = true)
    long countEntriesWithRheaReaction();

    @Query(value = "SELECT COUNT(distinct uniprot_accession) FROM ENZYME_PORTAL_REACTION WHERE kegg_id IS NOT NULL", nativeQuery = true)
    long countEntriesWithKegg();

    @Query(value = "SELECT COUNT(DISTINCT kegg_id) FROM ENZYME_PORTAL_REACTION", nativeQuery = true)
    long countUniqueKeggIds();

}
