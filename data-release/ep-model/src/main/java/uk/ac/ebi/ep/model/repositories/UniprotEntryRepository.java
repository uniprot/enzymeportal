package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.ep.model.UniprotEntry;

/**
 *
 * @author joseph
 */
//@RepositoryRestResource(excerptProjection = ProjectedSpecies.class)
@RepositoryRestResource(itemResourceRel = "uniprotEntry", collectionResourceRel = "uniprotEntry", path = "uniprotEntry")
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long>{

    default UniprotEntry findByUniprotAccession(String acc) {
        return findByAccession(acc);
    }

    UniprotEntry findByAccession(String accession);

    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY", nativeQuery = true)
    List<String> findAccessions();

    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY WHERE ENTRY_TYPE=0", nativeQuery = true)
    List<String> findSwissProtAccessions();
    
        @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UNIPROT_ENTRY SET EXP_EVIDENCE_FLAG = 1 WHERE ACCESSION IN (SELECT distinct accession from SP_ENZYME_EVIDENCE)", nativeQuery = true)
    void updateExpEvidenceFlag();

    
    

}
