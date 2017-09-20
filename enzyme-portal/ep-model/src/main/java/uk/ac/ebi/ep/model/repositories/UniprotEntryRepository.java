package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import javax.persistence.QueryHint;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.UniprotEntry;

/**
 *
 * @author joseph
 */
//@RepositoryRestResource(excerptProjection = ProjectedSpecies.class)
@RepositoryRestResource(itemResourceRel = "uniprotEntry", collectionResourceRel = "uniprotEntry", path = "uniprotEntry")
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long>, QueryDslPredicateExecutor<UniprotEntry>, JpaSpecificationExecutor<UniprotEntry>,DataStreamingService, UniprotEntryRepositoryCustom {

    default UniprotEntry findByUniprotAccession(String acc) {
        return findByAccession(acc);
    }

    UniprotEntry findByAccession(String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM UNIPROT_ENTRY WHERE ACCESSION = :ACCESSION ", nativeQuery = true)
    UniprotEntry findEnzymeByAccession(@Param("ACCESSION") String accession);

//            @Transactional(readOnly = true)
//    @Query(value = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession")
//    CompletableFuture<UniprotEntry> findEnzymeByAccessionAsync(@Param("accession") String accession);
    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY WHERE ACCESSION IS NOT NULL", nativeQuery = true)
    List<String> findAccessions();

    @Query(value = "SELECT ACCESSION FROM UNIPROT_ENTRY WHERE ENTRY_TYPE=0 AND ACCESSION IS NOT NULL", nativeQuery = true)
    List<String> findSwissProtAccessions();

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ *  FROM UNIPROT_ENTRY", nativeQuery = true)
    List<UniprotEntry> findUniprotEntries();
    
        @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ *  FROM UNIPROT_ENTRY WHERE ROWNUM < 100", nativeQuery = true)
    List<UniprotEntry> select100UniprotEntries();

    @Transactional(readOnly = true)
    //@Query(value = "SELECT * from UNIPROT_ENTRY u JOIN ENZYME_PORTAL_EC_NUMBERS e ON u.ACCESSION=e.UNIPROT_ACCESSION WHERE e.EC_NUMBER= :EC_NUMBER", nativeQuery = true)
    @Query(value = "SELECT * FROM UNIPROT_ENTRY u, ENZYME_PORTAL_EC_NUMBERS ec WHERE u.ACCESSION = ec.UNIPROT_ACCESSION AND ec.EC_NUMBER = :EC_NUMBER", nativeQuery = true)
    List<UniprotEntry> findEnzymesByEc(@Param("EC_NUMBER") String ecNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CATALYTIC_ACTIVITY FROM ENZYME_CATALYTIC_ACTIVITY WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findCatalyticActivitiesByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM UNIPROT_ENTRY WHERE ACCESSION IN (:ACCESSION)", nativeQuery = true)
    List<UniprotEntry> findSummariesByAccessions(@Param("ACCESSION") List<String> accession);

    @Query(value = "SELECT * FROM UNIPROT_ENTRY WHERE ACCESSION IN (:ACCESSION) /*#pageable}*/",
            countQuery = "SELECT count(*) FROM UNIPROT_ENTRY WHERE ACCESSION IN (:ACCESSION)",
            nativeQuery = true)
    Page<UniprotEntry> findSummariesByAccessions(@Param("ACCESSION") List<String> accession, Pageable pageable);

    @Transactional(readOnly = true)
    Page<UniprotEntry> findDistinctByAccessionIn(List<String> accessions, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(value = "UniprotEntryEntityGraph", type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "SELECT u FROM UniprotEntry u WHERE u.accession = :accession")
    List<UniprotEntry> findEnzymePortalByAccession(@Param("accession") String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ FIRST_ROWS(1000) PARALLEL(4) */ ACCESSION FROM UNIPROT_ENTRY WHERE ACCESSION IN (:ACCESSION)", nativeQuery = true)
    List<String> filterEnzymesFromAccessions(@Param("ACCESSION") List<String> accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT u FROM UniprotEntry u WHERE u.accession IN (:ACCESSION)")
    Stream<List<UniprotEntry>> findStreamedSummariesByAccessions(@Param("ACCESSION") List<String> accessions, Pageable pageable);

    @Query(value = "SELECT u FROM UniprotEntry u WHERE u.accession IN (:ACCESSION)")
    Slice<UniprotEntry> findSlicedSummariesByAccessions(@Param("ACCESSION") List<String> accessions, Pageable pageable);

    @Query(value = "SELECT u FROM UniprotEntry u WHERE u.accession IN (:ACCESSION)")
    @Async
    Future<List<UniprotEntry>> findFutureSummariesByAccessions(@Param("ACCESSION") List<String> accessions);

    @Transactional(readOnly = true)
    @Query(value = "SELECT e FROM UniprotEntry e "
            + "left join e.enzymePortalEcNumbersSet ec "
            + "WHERE ec.uniprotAccession = e.accession "
            + "AND ec.ecNumber = :ecNumber order by e.entryType ASC")

    Page<UniprotEntry> findEnzymesByEcNumber(@Param("ecNumber") String ecNumber, Pageable pageable);

    @Query(value = "SELECT DISTINCT e FROM UniprotEntry e "
            + "left join e.enzymePortalEcNumbersSet ec "
            + "WHERE ec.uniprotAccession = e.accession "
            + "AND ec.ecNumber = :ecNumber order by e.entryType ASC")
    Slice<UniprotEntry> findSlicedEnzymesByEcNumber(@Param("ecNumber") String ecNumber, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = "select ACCESSION from enzyme_portal_ec_numbers_mv where ec_number = :EC_NUMBER", nativeQuery = true)
    List<String> findAccessionViewByEc(@Param("EC_NUMBER") String ec);

    @Transactional(readOnly = true)
    @Query(value = "select ACCESSION from enzyme_portal_ec_numbers_mv where ec_number = :EC_NUMBER and SCIENTIFIC_NAME in (:SCIENTIFIC_NAME)", nativeQuery = true)
    List<String> findAccessionViewByEcAndSpecies(@Param("EC_NUMBER") String ec, @Param("SCIENTIFIC_NAME") List<String> species);

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = true)
    @Query(value = "UPDATE UNIPROT_ENTRY SET EXP_EVIDENCE_FLAG = 1 WHERE ACCESSION IN (SELECT distinct accession from SP_ENZYME_EVIDENCE)", nativeQuery = true)
    void updateExpEvidenceFlag();

    @Query(value = "SELECT * from UNIPROT_ENTRY u JOIN ENZYME_PORTAL_EC_NUMBERS e ON u.ACCESSION=e.UNIPROT_ACCESSION WHERE e.EC_NUMBER= :EC_NUMBER AND u.ENTRY_TYPE=0", nativeQuery = true)
    List<UniprotEntry> findSwissprotEnzymesByEc(@Param("EC_NUMBER") String ecNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ *  FROM UNIPROT_ENTRY ORDER BY ENTRY_TYPE ASC", nativeQuery = true)
    List<UniprotEntry> findUniprotEntriesOrderedByEntryType();

    @QueryHints(value = {
        @QueryHint(name = "org.hibernate.ScrollMode", value = "FORWARD_ONLY"),
        @QueryHint(name = HINT_FETCH_SIZE, value = "" + 900)
    })

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */ COUNT(*) FROM UNIPROT_ENTRY", nativeQuery = true)
    Long countUniprotEntries();

    @Query(value = "SELECT e.accession FROM UniprotEntry e")
    Stream<String> streamProteinIds();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM UNIPROT_ENTRY WHERE PROTEIN_GROUP_ID = :PROTEIN_GROUP_ID ", nativeQuery = true)
    List<UniprotEntry> findEnzymesByProteinGroupId(@Param("PROTEIN_GROUP_ID") String proteinGroupId);
    
}
