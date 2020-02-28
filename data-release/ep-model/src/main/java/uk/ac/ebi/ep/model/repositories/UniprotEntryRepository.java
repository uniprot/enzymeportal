package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.model.UniprotEntry;

/**
 *
 * @author joseph
 */
@Repository
//@RepositoryRestResource(excerptProjection = ProjectedSpecies.class)
//@RepositoryRestResource(itemResourceRel = "uniprotEntry", collectionResourceRel = "uniprotEntry", path = "uniprotEntry")
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long> {

    default UniprotEntry findByUniprotAccession(String acc) {
        return findByAccession(acc);
    }

    UniprotEntry findByAccession(String accession);

    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY", nativeQuery = true)
    List<String> findAccessions();

    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY WHERE ENTRY_TYPE=0", nativeQuery = true)
    List<String> findSwissProtAccessions();

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UNIPROT_ENTRY SET EXP_EVIDENCE_FLAG = 1 WHERE ACCESSION IN (SELECT distinct accession from ENZYME_SP_EXP_EVIDENCE)", nativeQuery = true)
    void updateExpEvidenceFlag();

    @Query(value = "SELECT COUNT(ACCESSION) FROM UNIPROT_ENTRY", nativeQuery = true)
    long countUniprotEntries();

    @Query(value = "SELECT COUNT(ec_number) FROM intenz_enzymes WHERE transfer_flag='N'", nativeQuery = true)
    long countINTENZ();

    @Query(value = "SELECT COUNT( DISTINCT omim_number) FROM enzyme_portal_disease", nativeQuery = true)
    long countUniqueOmimIds();

    @Query(value = "SELECT COUNT(distinct uniprot_accession) FROM enzyme_portal_disease", nativeQuery = true)
    long countEntriesWithDiseases();

    @Query(value = "SELECT COUNT (distinct uniprot_accession) FROM ENZYME_CATALYTIC_ACTIVITY", nativeQuery = true)
    long countEntriesWithCatalyticActivities();

    @Query(value = "SELECT count(accession) FROM uniprot_entry WHERE exp_evidence_flag=1", nativeQuery = true)
    long countEntriesWithExpEvidence();

    @Query(value = "SELECT count(accession) FROM uniprot_entry WHERE entry_type=0", nativeQuery = true)
    long countReviewedEntries();

    @Query(value = "SELECT count(accession) FROM uniprot_entry WHERE entry_type=1", nativeQuery = true)
    long countUnreviewedEntries();

}
