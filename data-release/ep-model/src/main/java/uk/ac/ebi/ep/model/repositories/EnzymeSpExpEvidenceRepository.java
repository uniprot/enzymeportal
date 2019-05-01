
package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ep.model.EnzymeSpExpEvidence;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface EnzymeSpExpEvidenceRepository extends JpaRepository<EnzymeSpExpEvidence, Long>{
    
}
