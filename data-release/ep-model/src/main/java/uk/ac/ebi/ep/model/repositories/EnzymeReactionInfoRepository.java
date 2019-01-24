package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.model.EnzymeReactionInfo;
import uk.ac.ebi.ep.model.search.model.ReactionInfoView;

/**
 *
 * @author joseph
 */
public interface EnzymeReactionInfoRepository extends JpaRepository<EnzymeReactionInfo, Long> {

    @Query(value = "SELECT * FROM enzyme_reaction_info WHERE xref_type = :xref_type", nativeQuery = true)
    List<EnzymeReactionInfo> findReactionInfoByXrefType(@Param("xref_type") String xrefType);

    @Query(value = "SELECT  uniprotAccession, xref FROM EnzymeReactionInfo WHERE xrefType=:xrefType")
    List<ReactionInfoView> findReactionInfoViewByXrefType(@Param("xrefType") String xrefType);

    @Query("SELECT e FROM EnzymeReactionInfo e WHERE e.xrefType=:xrefType")
    Stream<EnzymeReactionInfo> findAllReactionInfoByXrefTypeAndStream(@Param("xrefType") String xrefType);

    @Query("SELECT e FROM EnzymeReactionInfo e where e.xrefType=:xrefType and rownum <= :rownum")
    Stream<EnzymeReactionInfo> streamLimitedReactionInfoByXrefType(@Param("xrefType") String xrefType, @Param("rownum") Long rownum);

    @Query("SELECT e.xref as xref, e.uniprotAccession as uniprotAccession FROM EnzymeReactionInfo e WHERE e.xrefType=:xrefType")
    Stream<ReactionInfoView> findAllReactionInfoViewByXrefTypeAndStream(@Param("xrefType") String xrefType);

    @Query(value = "SELECT COUNT(*) FROM enzyme_reaction_info WHERE xref_type = :xref_type", nativeQuery = true)
    Long countReactionInfoByXrefType(@Param("xref_type") String xrefType);

}
