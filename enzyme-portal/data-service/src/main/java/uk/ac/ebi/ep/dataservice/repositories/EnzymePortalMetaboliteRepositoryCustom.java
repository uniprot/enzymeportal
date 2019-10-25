
package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.Metabolite;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalMetaboliteRepositoryCustom {
    
     List<Metabolite> findMetaboliteNameLike(String name);
}
