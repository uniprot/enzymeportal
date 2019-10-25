package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
interface EnzymePortalCofactorRepositoryCustom {

    List<Cofactor> cofactorsNameLike(String name);
}
