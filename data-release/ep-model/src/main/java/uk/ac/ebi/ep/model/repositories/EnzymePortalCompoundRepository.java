package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymePortalCompound;

/**
 *
 * @author joseph
 */
public interface EnzymePortalCompoundRepository extends JpaRepository<EnzymePortalCompound, Long>{//, QuerydslPredicateExecutor<EnzymePortalCompound>, EnzymePortalCompoundRepositoryCustom {

    List<EnzymePortalCompound> findByCompoundName(String name);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_COMPOUND,IX_ENZYME_COMPOUND_DUPS) */ INTO ENZYME_PORTAL_COMPOUND "
            + "(COMPOUND_ID,COMPOUND_NAME,COMPOUND_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE,NOTE) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void createCompoundIgnoreDup(String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT /*+ ignore_row_on_dupkey_index(ENZYME_PORTAL_COMPOUND, IX_ENZYME_COMPOUND_DUPS) */ INTO ENZYME_PORTAL_COMPOUND (COMPOUND_ID, COMPOUND_NAME, COMPOUND_SOURCE, RELATIONSHIP,UNIPROT_ACCESSION,URL, COMPOUND_ROLE, NOTE)\n"
            + "SELECT COMPOUND_ID, COMPOUND_NAME, COMPOUND_SOURCE, RELATIONSHIP,UNIPROT_ACCESSION,URL, COMPOUND_ROLE, NOTE FROM TEMP_COMPOUND_COMPARE tcc WHERE tcc.uniprot_accession IN (SELECT accession FROM UNIPROT_ENTRY)", nativeQuery = true)
    void insertCompounds();

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_COMPOUND WHERE UNIPROT_ACCESSION IN (:UNIPROT_ACCESSION)", nativeQuery = true)
    List<EnzymePortalCompound> findCompoundsByAccessions(@Param("UNIPROT_ACCESSION") List<String> accession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ENZYME_PORTAL_COMPOUND WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<EnzymePortalCompound> findCompoundsByAccession(@Param("UNIPROT_ACCESSION") String accession);



}
