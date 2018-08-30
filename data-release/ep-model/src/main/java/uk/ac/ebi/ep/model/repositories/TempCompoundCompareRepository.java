package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.model.TempCompoundCompare;

/**
 *
 * @author joseph
 */
public interface TempCompoundCompareRepository extends JpaRepository<TempCompoundCompare, Long> {

    @Modifying
    @Query(value = "Insert INTO TEMP_COMPOUND_COMPARE "
            + "(PRIMARY_TARGET_ID,COMPOUND_ID,COMPOUND_NAME,COMPOUND_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE,NOTE) VALUES (?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
    void addTempCompounds(String primaryTargetId, String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note);

    @Query(value = "SELECT  DISTINCT UNIPROT_ACCESSION FROM CHEMBL_TARGETS", nativeQuery = true)
    List<String> findUniqueTargetedproteins();

    @Query(value = "SELECT CHEMBL_ID FROM CHEMBL_TARGETS WHERE UNIPROT_ACCESSION =:UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findTargetetsByProtein(@Param("UNIPROT_ACCESSION") String accession);

}
