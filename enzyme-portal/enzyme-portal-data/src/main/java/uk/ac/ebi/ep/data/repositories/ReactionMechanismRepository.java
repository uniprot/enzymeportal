package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.data.domain.ReactionMechanism;

/**
 *
 * @author Joseph
 */
@Repository
public interface ReactionMechanismRepository extends JpaRepository<ReactionMechanism, String> {

   // @Query(value = "SELECT m.MCSA_ID,m.ENZYME_NAME,m.IMAGE_ID,m.MECHANISM_DESCRIPTION FROM REACTION_MECHANISM m,REACTION_MECHANISM_2_UNIPROT up WHERE up.MCSA_ID=m.MCSA_ID  AND up.ACCESSION= :ACCESSION", nativeQuery = true)
   // List<ReactionMechanismView> findReactionMechanismsByAccession(@Param("ACCESSION") String accession);

    //@Query(value = "SELECT m.MCSA_ID,m.ENZYME_NAME,m.IMAGE_ID,m.MECHANISM_DESCRIPTION FROM REACTION_MECHANISM m,REACTION_MECHANISM_2_UNIPROT up WHERE up.MCSA_ID=m.MCSA_ID  AND up.ACCESSION= :ACCESSION", nativeQuery = true)
   // ReactionMechanismView findReactionMechanismByAccession(@Param("ACCESSION") String accession);

    @Query(value = "SELECT m.MCSA_ID,m.ENZYME_NAME,m.IMAGE_ID,m.MECHANISM_DESCRIPTION FROM REACTION_MECHANISM m,REACTION_MECHANISM_2_UNIPROT up WHERE up.MCSA_ID=m.MCSA_ID  AND up.ACCESSION= :ACCESSION", nativeQuery = true)
    List<ReactionMechanism> findReactionMechanismsByAccession(@Param("ACCESSION") String accession);

    @Query(value = "SELECT * FROM REACTION_MECHANISM WHERE MCSA_ID= :MCSA_ID", nativeQuery = true)
    List<ReactionMechanism> findReactionMechanismsByMcsaId(@Param("MCSA_ID") String mcsaId);
}
