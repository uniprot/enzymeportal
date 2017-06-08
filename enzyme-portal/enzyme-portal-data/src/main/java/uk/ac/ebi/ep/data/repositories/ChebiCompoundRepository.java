package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.ChebiCompound;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface ChebiCompoundRepository extends JpaRepository<ChebiCompound, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM CHEBI_COMPOUND WHERE CHEBI_ACCESSION = :CHEBI_ACCESSION ", nativeQuery = true)
    ChebiCompound findByChebiAccession(@Param("CHEBI_ACCESSION") String chebiAccession);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM CHEBI_COMPOUND WHERE COMPOUND_NAME = :COMPOUND_NAME ", nativeQuery = true)
    ChebiCompound findByCompoundName(@Param("COMPOUND_NAME") String compoundName);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM CHEBI_COMPOUND WHERE CHEBI_ACCESSION = :CHEBI_ACCESSION AND SOURCE = :SOURCE", nativeQuery = true)
    List<ChebiCompound> findByChebiAccession(@Param("CHEBI_ACCESSION") String chebiAccession, @Param("SOURCE") String source);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT /*+ PARALLEL(auto) */ * FROM CHEBI_COMPOUND WHERE COMPOUND_NAME = :COMPOUND_NAME AND SOURCE = :SOURCE", nativeQuery = true)
    List<ChebiCompound> findByCompoundName(@Param("COMPOUND_NAME") String compoundName, @Param("SOURCE") String source);
}