
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalSummary;

/**
 *
 * @author joseph
 */
public interface EnzymePortalSummaryRepository extends JpaRepository<EnzymePortalSummary, Long>, QueryDslPredicateExecutor<EnzymePortalSummary>, EnzymePortalSummaryRepositoryCustom {

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE='REGULATION' ", nativeQuery = true)
    List<EnzymePortalSummary> findSummariesByRegulation();

//    @Transactional(readOnly = true)
//    //@Query(value = "SELECT * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TEXT IS NOT NULL AND ROWNUM <= 2000 AND COMMENT_TYPE = :COMMENT_TYPE ", nativeQuery = true)
//    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE", nativeQuery = true)
//    List<EnzymePortalSummary> findSummariesByCommentType(@Param("COMMENT_TYPE") String commentType);
//    
    
//     @Transactional(readOnly = true)
//    @Query(name ="findCommentTextAndAccession", value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE", nativeQuery = true)
//    List<Summary> findSummariesByCommentType(@Param("COMMENT_TYPE") String commentType);
//    
//         @Transactional(readOnly = true)
//    @Query( value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
//    EnzymePortalSummary findDiseaseEvidence(@Param("COMMENT_TYPE") String commentType,@Param("UNIPROT_ACCESSION") String accession);
// 
    
        @Transactional(readOnly = true)
    @Query( value = "SELECT DISTINCT /*+ PARALLEL(auto) */ COMMENT_TEXT  FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = 'DISEASE' AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    String findDiseaseEvidence( @Param("UNIPROT_ACCESSION") String accession);


    @Transactional(readOnly = true)
    @Query(value = "SELECT COMMENT_TEXT FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE='CATALYTIC_ACTIVITY' AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findCatalyticActivitiesByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COMMENT_TEXT FROM ENZYME_PORTAL_SUMMARY WHERE COMMENT_TYPE = :COMMENT_TYPE AND UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> FindTextByCommentTypeAndAccession(@Param("COMMENT_TYPE") String commentType, @Param("UNIPROT_ACCESSION") String accession);

}
