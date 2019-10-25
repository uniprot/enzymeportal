package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.entities.UniprotXref;

/**
 *
 * @author joseph
 */
@Repository
public interface UniprotXrefRepository extends JpaRepository<UniprotXref, Long> {

    @Query(value = "SELECT SOURCE_ID as id, SOURCE_NAME as name FROM UNIPROT_XREF WHERE SOURCE='PDB' AND ACCESSION=:ACCESSION ", nativeQuery = true)
    List<PdbView> findPdbViewsByAccession(@Param("ACCESSION") String accession);

}
