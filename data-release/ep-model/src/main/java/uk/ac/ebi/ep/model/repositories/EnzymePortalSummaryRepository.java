package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalSummary;
import uk.ac.ebi.ep.model.dao.SummaryView;

/**
 *
 * @author joseph
 */
public interface EnzymePortalSummaryRepository extends JpaRepository<EnzymePortalSummary, Long>, EnzymePortalSummaryRepositoryCustom {

    @Transactional(readOnly = true)
    @Query("SELECT s.uniprotAccession.accession as accession, s.commentText as commentText FROM EnzymePortalSummary s WHERE s.commentType=:commentType and rownum <= 10")
    Stream<SummaryView> streamSummaryByCommentType(@Param("commentType") String commentType);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COUNT(UNIPROT_ACCESSION) FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE", nativeQuery = true)
    Long countSummaryByCommentType(@Param("COMMENT_TYPE") String commentType);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE='REGULATION' ", nativeQuery = true)
    List<EnzymePortalSummary> findSummariesByRegulation();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ COMMENT_TEXT  FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = 'DISEASE' AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    String findDiseaseEvidence(@Param("UNIPROT_ACCESSION") String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COMMENT_TEXT FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE='CATALYTIC_ACTIVITY' AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findCatalyticActivitiesByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COMMENT_TEXT FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> FindTextByCommentTypeAndAccession(@Param("COMMENT_TYPE") String commentType, @Param("UNIPROT_ACCESSION") String accession);

}
