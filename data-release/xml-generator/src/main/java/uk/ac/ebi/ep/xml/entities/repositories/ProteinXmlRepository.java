package uk.ac.ebi.ep.xml.entities.repositories;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.xml.entities.ProteinXml;

/**
 *
 * @author joseph
 */
@Repository
public interface ProteinXmlRepository extends JpaRepository<ProteinXml, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT p FROM ProteinXml p WHERE p.proteinGroupId = :proteinGroupId")
    Stream<ProteinXml> streamProteinDataByProteinGroupId(@Param("proteinGroupId") String proteinGroupId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM ProteinXml p WHERE p.ecNumber = :ecNumber")
    Stream<ProteinXml> streamProteinDataByEcNumber(@Param("ecNumber") String ecNumber);
}
