package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;

/**
 *
 * @author joseph
 */
public interface EnzymePortalCofactorService {

    List<EnzymePortalCofactor> cofactors();

    Page<EnzymePortalCofactor> cofactors(Pageable pageable);

    List<Cofactor> findCofactorsLike(String cofactorName);
}
