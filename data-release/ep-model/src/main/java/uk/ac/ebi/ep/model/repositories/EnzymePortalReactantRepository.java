package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalReactant;

/**
 *
 * @author joseph
 */
public interface EnzymePortalReactantRepository extends JpaRepository<EnzymePortalReactant, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_REACTANT,ENZYME_PORTAL_REACTANT_UK1) */ INTO ENZYME_PORTAL_REACTANT "
            + "(REACTANT_ID,REACTANT_NAME,REACTANT_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,REACTANT_ROLE,REACTANT_DIRECTION) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void createReactantIgnoreDup(String reactantId, String reactantName, String reactantSource, String relationship, String accession, String url, String reactantRole, String reactantDirection);

}
