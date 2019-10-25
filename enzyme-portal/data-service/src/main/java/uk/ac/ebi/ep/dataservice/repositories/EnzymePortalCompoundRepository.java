
package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCompound;

/**
 *
 * @author joseph
 */
@Repository
public interface EnzymePortalCompoundRepository extends JpaRepository<EnzymePortalCompound, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT  COMPOUND_ID as id,COMPOUND_NAME as name, COMPOUND_SOURCE as source,URL as url,COMPOUND_ROLE as role FROM ENZYME_PORTAL_COMPOUND WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<CompoundView> findCompoundsByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CHEMBL_TARGET_ID FROM ENZYME_PORTAL_COMPOUND WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findChemblTargetIdByAccession(@Param("UNIPROT_ACCESSION") String accession);

}
