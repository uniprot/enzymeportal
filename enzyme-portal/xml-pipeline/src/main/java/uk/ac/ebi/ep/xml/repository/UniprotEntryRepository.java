package uk.ac.ebi.ep.xml.repository;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.xml.entity.UniprotEntry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long> {

    @Transactional
    @Query(value = "SELECT u FROM UniprotEntry u INNER JOIN u.enzymePortalEcNumbersSet e WHERE u.accession = e.uniprotAccession AND e.ecNumber= :ecNumber")
    Stream<UniprotEntry> streamEnzymesByEc(@Param("ecNumber") String ecNumber);

}
