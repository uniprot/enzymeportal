
package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ep.model.EnzymeReactionInfoPart;

/**
 *
 * @author joseph
 */
public interface EnzymeReactionInfoPartRepository extends JpaRepository<EnzymeReactionInfoPart, Long> {
   
//        @Query("SELECT e FROM EnzymeReactionInfoPart e  WHERE e.xrefType=:xrefType AND e.xref='CHEBI:30839'")
//    Stream<EnzymeReactionInfoPart> streamReactionInfoByPartitionAndXrefType(@Param("xrefType") String xrefType);

}
