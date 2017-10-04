package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;

/**
 *
 * @author joseph
 */
public interface EnzymePortalPathwaysRepository extends JpaRepository<EnzymePortalPathways, Long>, QueryDslPredicateExecutor<EnzymePortalPathways>, EnzymePortalPathwaysRepositoryCustom {

    @Query(value = "SELECT * FROM ENZYME_PORTAL_PATHWAYS", nativeQuery = true)
    List<EnzymePortalPathways> findPathways();

    EnzymePortalPathways findByPathwayId(Long pathwayId);

    @Query(value = "SELECT * FROM ENZYME_PORTAL_PATHWAYS  WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalPathways> findPathwaysByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Modifying
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_PATHWAYS,IX_ENZYME_PATHWAY_DUPS) */ INTO ENZYME_PORTAL_PATHWAYS "
            + "(UNIPROT_ACCESSION,PATHWAY_ID,PATHWAY_URL,PATHWAY_NAME,STATUS,SPECIES) VALUES (?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    void createPathwayIgnoreDup(String accession, String pathwayId, String pathwayUrl, String pathwayName, String status, String species);

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */  DISTINCT(UNIPROT_ACCESSION) FROM ENZYME_PORTAL_PATHWAYS WHERE LOWER(PATHWAY_NAME) = :PATHWAY_NAME", nativeQuery = true)
    List<String> findAccessionsByPathwayName(@Param("PATHWAY_NAME") String pathwayName);

    @Query(value = "SELECT /*+ PARALLEL(auto) */  DISTINCT(UNIPROT_ACCESSION) FROM ENZYME_PORTAL_PATHWAYS WHERE PATHWAY_ID = :PATHWAY_ID", nativeQuery = true)
    List<String> findAccessionsByPathwayId(@Param("PATHWAY_ID") String pathwayId);

}
