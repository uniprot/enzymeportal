package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalMetabolite;
import uk.ac.ebi.ep.model.dao.MetaboliteView;

/**
 *
 * @author joseph
 */
public interface EnzymePortalMetaboliteRepository extends JpaRepository<EnzymePortalMetabolite, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert INTO ENZYME_PORTAL_METABOLITE (METABOLITE_INTERNAL_ID,METABOLITE_ID,METABOLITE_NAME,METABOLITE_URL) VALUES (SEQ_EZP_METABOLITE.NEXTVAL,?1,?2,?3) ", nativeQuery = true)
    void createMetabolite(String metaboliteId, String metaboliteName, String metaboliteUrl);

    @Transactional(readOnly = true)
    @Query(value = "select DISTINCT c.compoundName as compoundName, c.compoundId as compoundId from EnzymePortalChebiCompound c WHERE c.compoundRole='METABOLITE' order by c.compoundName, c.compoundId")
    List<MetaboliteView> findMetabolites();

    @Query(value = "SELECT COUNT( metabolite_id) FROM enzyme_portal_metabolite", nativeQuery = true)
    long countUniqueMetabolightsIds();

    @Query(value = " SELECT count(distinct uniprot_accession) from enzyme_portal_chebi_compound where compound_role='METABOLITE'", nativeQuery = true)
    long countEntriesWithMetabolites();
}
