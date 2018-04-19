package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.ChemblTargets;

/**
 *
 * @author Joseph
 */
public interface ChemblTargetsRepository extends JpaRepository<ChemblTargets, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "ALTER TABLE CHEMBL_TARGETS DISABLE CONSTRAINT CHEMBL_TARGETS_FK1", nativeQuery = true)
    void disableTargetContraints();

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    @Query(value = "insert into CHEMBL_TARGETS VALUES (seq_chembl_targets_id.nextval,?1,?2,?3)", nativeQuery = true)
    void addChemblTargets(String chemblId, String componentType, String accession);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "delete from CHEMBL_TARGETS where UNIPROT_ACCESSION not in (select accession from UNIPROT_ENTRY)", nativeQuery = true)
    void deleteNonEnzymesTargets();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "ALTER TABLE CHEMBL_TARGETS ENABLE CONSTRAINT CHEMBL_TARGETS_FK1", nativeQuery = true)
    void enableTargetContraints();

    @Query(value = "SELECT * FROM CHEMBL_TARGETS", nativeQuery = true)
    List<ChemblTargets> getAllChemblTargets();
}
