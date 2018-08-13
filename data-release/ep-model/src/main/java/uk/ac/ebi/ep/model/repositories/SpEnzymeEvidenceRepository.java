
package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ep.model.SpEnzymeEvidence;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface SpEnzymeEvidenceRepository extends JpaRepository<SpEnzymeEvidence, Long>{
    
}
