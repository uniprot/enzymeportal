package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalReaction;

/**
 *
 * @author Joseph
 */
@Repository
public interface EnzymePortalReactionRepository extends JpaRepository<EnzymePortalReaction, Long> {

    @Query(value = "SELECT REACTION_ID as id,REACTION_NAME as name,KEGG_ID as keggId FROM ENZYME_PORTAL_REACTION  WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymeReactionView> findReactionsByAccession(@Param("UNIPROT_ACCESSION") String accession);

}
