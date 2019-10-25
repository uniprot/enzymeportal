
package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.Disease;

/**
 * @author joseph
 */
@NoRepositoryBean
public interface DiseaseRepositoryCustom {

    List<Disease> findDiseasesNameLike(String name);

}
