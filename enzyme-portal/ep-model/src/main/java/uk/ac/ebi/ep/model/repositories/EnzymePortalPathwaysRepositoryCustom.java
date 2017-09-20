
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.model.search.model.Pathway;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalPathwaysRepositoryCustom {
    

     //List<String> findAccessionsByPathwayId(String pathwayId);
    List<Pathway> findPathwaysByName(String pathwayName);
}
