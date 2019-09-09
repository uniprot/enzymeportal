package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymeReactionInfo;
import uk.ac.ebi.ep.model.dao.ChebiReactant;
import uk.ac.ebi.ep.model.dao.ReactionInfoView;

/**
 *
 * @author joseph
 */
@Repository
public interface EnzymeReactionInfoRepository extends JpaRepository<EnzymeReactionInfo, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT  e.xref as chebiId,e.uniprotAccession as accession FROM EnzymeReactionInfo e WHERE e.xrefType='CHEBI'")
    Stream<ChebiReactant> streamChebiReactantInfo();

    @Transactional(readOnly = true)
    @Query(value = "SELECT COUNT(xref) FROM enzyme_reaction_info WHERE xref_type='CHEBI'", nativeQuery = true)
    Long countChebiReactantInfo();

    @Query(value = "SELECT * FROM enzyme_reaction_info WHERE xref_type = :xref_type", nativeQuery = true)
    List<EnzymeReactionInfo> findReactionInfoByXrefType(@Param("xref_type") String xrefType);

    @Query(value = "SELECT  uniprotAccession, xref FROM EnzymeReactionInfo WHERE xrefType=:xrefType")
    List<ReactionInfoView> findReactionInfoViewByXrefType(@Param("xrefType") String xrefType);

    @Transactional
    @Query("SELECT e FROM EnzymeReactionInfo e WHERE xrefType=:xrefType")
    Stream<EnzymeReactionInfo> findAllReactionInfoByXrefTypeAndStream(@Param("xrefType") String xrefType);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM EnzymeReactionInfo e")
    Stream<EnzymeReactionInfo> findAllReactionInfoByXrefTypeAndStream();

    @Query("SELECT e FROM EnzymeReactionInfo e where e.xrefType=:xrefType and rownum <= :rownum")
    Stream<EnzymeReactionInfo> streamLimitedReactionInfoByXrefType(@Param("xrefType") String xrefType, @Param("rownum") Long rownum);

    @Query("SELECT e.xref as xref, e.uniprotAccession as uniprotAccession FROM EnzymeReactionInfo e WHERE e.xrefType=:xrefType")
    Stream<ReactionInfoView> findAllReactionInfoViewByXrefTypeAndStream(@Param("xrefType") String xrefType);

    @Query(value = "SELECT COUNT(*) FROM enzyme_reaction_info WHERE xref_type = :xref_type", nativeQuery = true)
    Long countReactionInfoByXrefType(@Param("xref_type") String xrefType);

    @Query("SELECT e FROM EnzymeReactionInfo e WHERE ROWID IN ( SELECT MAX(ROWID) FROM EnzymeReactionInfo WHERE xrefType='CHEBI' GROUP BY xref )")
    Stream<EnzymeReactionInfo> findReactionInfoByChebiAndStream();

    @Query(value = "SELECT COUNT (distinct xref) FROM enzyme_reaction_info where xref_type = :xref_type", nativeQuery = true)
    Long countDistinctReactionInfoByXrefType(@Param("xref_type") String xrefType);

    @Query("SELECT e FROM EnzymeReactionInfo e WHERE ROWID IN ( SELECT MAX(ROWID) FROM EnzymeReactionInfo WHERE xrefType=:xrefType GROUP BY xref )")
    Stream<EnzymeReactionInfo> findUniqueXrefReactionInfoByXrefTypeAndStream(@Param("xrefType") String xrefType);

}
