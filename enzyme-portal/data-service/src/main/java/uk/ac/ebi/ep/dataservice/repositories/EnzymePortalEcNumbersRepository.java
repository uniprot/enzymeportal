package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalEcNumbers;

/**
 *
 * @author joseph
 */
@Repository
public interface EnzymePortalEcNumbersRepository extends JpaRepository<EnzymePortalEcNumbers, Long> {

   
    @Query(value = "SELECT DISTINCT EC_NUMBER FROM ENZYME_PORTAL_EC_NUMBERS WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findEcNumbersByAccession(@Param("UNIPROT_ACCESSION") String accession);

}
