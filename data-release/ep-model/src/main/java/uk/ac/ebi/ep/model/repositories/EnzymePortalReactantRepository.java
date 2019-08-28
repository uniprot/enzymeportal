package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalReactant;
import uk.ac.ebi.ep.model.dao.ChebiReactant;

/**
 *
 * @author joseph
 */
public interface EnzymePortalReactantRepository extends JpaRepository<EnzymePortalReactant, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_REACTANT,ENZYME_PORTAL_REACTANT_UK1) */ INTO ENZYME_PORTAL_REACTANT "
            + "(REACTANT_INTERNAL_ID,REACTANT_ID,REACTANT_NAME,REACTANT_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,REACTANT_ROLE,REACTION_DIRECTION) VALUES (SEQ_REACTANT_INTERNAL_ID.NEXTVAL,?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void createReactantIgnoreDup(String reactantId, String reactantName, String reactantSource, String relationship, String accession, String url, String reactantRole, String reactionDirection);

    @Transactional
    //@Query("SELECT DISTINCT e.reactantId FROM EnzymePortalReactant e WHERE e.reactantSource='ChEBI' AND e.reactantId='CHEBI:30839'")
    @Query("SELECT DISTINCT e.reactantId FROM EnzymePortalReactant e WHERE e.reactantSource='ChEBI'")
    Stream<String> streamChebiIds();

    @Transactional
    @Query("SELECT e FROM EnzymePortalReactant e WHERE e.reactantSource='ChEBI'")
    Stream<EnzymePortalReactant> findChebiReactantsAndStream();

    @Transactional
    @Query("SELECT e.reactantId as chebiId,e.reactantName as chebiName,e.uniprotAccession.accession as accession FROM EnzymePortalReactant e WHERE e.reactantSource='ChEBI'")
    Stream<ChebiReactant> streamChebiReactants();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE enzyme_portal_reactant SET reactant_role = :reactant_role,relationship=:relationship WHERE reactant_id = :reactant_id", nativeQuery = true)
    void updateReactant(@Param("reactant_role") String reactant_role, @Param("relationship") String relationship, @Param("reactant_id") String reactant_id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_REACTANT WHERE reactant_id = :reactant_id", nativeQuery = true)
    List<EnzymePortalReactant> findEnzymePortalReactantByReactantId(@Param("reactant_id") String reactant_id);
}
