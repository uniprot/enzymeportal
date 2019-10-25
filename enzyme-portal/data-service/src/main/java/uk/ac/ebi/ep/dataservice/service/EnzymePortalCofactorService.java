package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;

/**
 *
 * @author joseph
 */
public interface EnzymePortalCofactorService {

    List<EnzymePortalCofactor> cofactors();

    List<Cofactor> findCofactorsLike(String cofactorName);
}
