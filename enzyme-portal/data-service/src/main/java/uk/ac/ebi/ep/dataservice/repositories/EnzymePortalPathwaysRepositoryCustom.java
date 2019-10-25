package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.Pathway;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalPathwaysRepositoryCustom {

    List<Pathway> findPathwaysWithNamesLike(String name);
}
